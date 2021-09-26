package com.example.android.politicalpreparedness.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch

class ElectionsViewModel(private val repository: ElectionRepository): ViewModel() {

    fun getElections() {
        viewModelScope.launch {
            repository.refreshElections()
        }
    }

    val currentElections = repository.currentElections
    val savedElections = repository.savedElections
    val result = repository.result

    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo
        get() = _navigateToVoterInfo

    fun onResultHandled() {
        repository.onResultHandled()
    }

    fun onElectionClicked(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun onVoterInfoNavigated() {
        _navigateToVoterInfo.value = null
    }
    //TODO: Create functions to navigate to saved or upcoming election voter info

}