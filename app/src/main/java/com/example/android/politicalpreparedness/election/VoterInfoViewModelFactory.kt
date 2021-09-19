package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.ElectionRepository

//TODO: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(
    private val application: Application,
    private val electionId: Int,
    private val division: Division
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            val repository = ElectionRepository(application)
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(repository, electionId, division) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}