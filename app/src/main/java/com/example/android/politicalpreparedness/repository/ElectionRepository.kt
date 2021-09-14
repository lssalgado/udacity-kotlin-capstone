package com.example.android.politicalpreparedness.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionRepository(val context: Context) {

    val database: ElectionDatabase by lazy { ElectionDatabase.getInstance(context)}

    val elections: LiveData<List<Election>> = database.electionDao.getElections()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val response = CivicsApi.retrofitService.getElections(BuildConfig.API_KEY)
            Timber.e("batata")
            Timber.e(response.toString())
            database.electionDao.insertAll(*response.elections.toTypedArray())
        }
    }
}