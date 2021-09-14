package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase

//TODO: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(val database: ElectionDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (VoterInfoViewModel(database.electionDao) as T)
}