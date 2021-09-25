package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(private val repository: ElectionRepository): ViewModel() {

    //TODO: Create live data val for upcoming elections
    fun getElections() {
        viewModelScope.launch {
            repository.refreshElections()
        }
    }

    val currentElections = repository.currentElections
    val savedElections = repository.savedElections
    val result = repository.result

    fun onResultHandled() {
        repository.onResultHandled()
    }

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}