package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.ElectionRepository

//TODO: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            val repository = ElectionRepository(application)
            @Suppress("UNCHECKED_CAST")
            return ElectionsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}