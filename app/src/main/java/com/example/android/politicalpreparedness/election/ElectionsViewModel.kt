package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun onResultHandled() {
        repository.onResultHandled()
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info

}