package com.example.android.politicalpreparedness.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionRepository(val context: Context) {

    val database: ElectionDatabase by lazy { ElectionDatabase.getInstance(context)}

    val currentElections: LiveData<List<Election>> = database.electionDao.getCurrentElections()
    val savedElections: LiveData<List<Election>> = database.electionDao.getSavedElections()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val response = CivicsApi.retrofitService.getElections(BuildConfig.API_KEY)
            Timber.e(response.toString())
            val newElections = response.elections.toTypedArray()
            deleteOldNonSavedElections(newElections)
            database.electionDao.insertAll(*newElections)
        }
    }

    /**
     * Database maintenance method used to remove all old elections that are not saved.
     */
    private suspend fun deleteOldNonSavedElections(elections: Array<Election>) {
        withContext(Dispatchers.IO) {
            elections.forEach { election ->
                if (!elections.contains(election)) {
                    if (!election.saved) {
                        database.electionDao.deleteElection(election.id)
                    } else if (!election.old){
                        election.old = true
                        database.electionDao.update(election)
                    }
                }
            }
        }
    }
}