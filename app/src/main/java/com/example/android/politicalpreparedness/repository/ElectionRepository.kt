package com.example.android.politicalpreparedness.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import com.example.android.politicalpreparedness.network.Result

class ElectionRepository(val context: Context) {

    val database: ElectionDatabase by lazy { ElectionDatabase.getInstance(context)}

    val currentElections: LiveData<List<Election>> = database.electionDao.getCurrentElections()
    val savedElections: LiveData<List<Election>> = database.electionDao.getSavedElections()
    private val _result = MutableLiveData<Result>()
    val result :LiveData<Result>
    get() = _result

    suspend fun refreshElections() {
        try {
            val response = CivicsApi.retrofitService.getElections()
            _result.value = Result.Success
            withContext(Dispatchers.IO) {
                val newElections = response.elections.toTypedArray()
                deleteOldNonSavedElections(newElections)
                database.electionDao.insertAll(*newElections)
            }
        } catch (e: HttpException) {
            _result.value = Result.HttpError(e.code())
            Timber.e(e)
        } catch (e: Exception) {
            _result.value = Result.Error(
                e.message ?: "An Exception was thrown when trying to fetch elections!!"
            )
            Timber.e(e)
        }
    }

    fun onResultHandled() {
        _result.value = null
    }

    fun getElectionById(id: Int): LiveData<Election?> {
        return database.electionDao.getElectionById(id)
    }

    suspend fun updateElection(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.update(election)
        }
    }

    suspend fun deleteElection(id: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.deleteElection(id)
        }
    }

    /**
     * Database maintenance method used to remove all old elections that are not saved.
     */
    private suspend fun deleteOldNonSavedElections(newElections: Array<Election>) {
        withContext(Dispatchers.IO) {
            currentElections.value?.let { currElections ->
                currElections.forEach { election ->
                    val existingElection = newElections.find{ election.id == it.id }
                    // If no election with a matching id is found
                    if (existingElection == null) {
                        // And the current election is not saved
                        if (!election.saved) {
                            // The election is deleted
                            database.electionDao.deleteElection(election.id)
                        } else if (!election.old) {
                            // The flag is updated
                            election.old = true
                            database.electionDao.update(election)
                        }
                    } else {
                        // Else the current saved value is saved
                        existingElection.saved = election.saved
                    }
                }
            }
        }
    }
}