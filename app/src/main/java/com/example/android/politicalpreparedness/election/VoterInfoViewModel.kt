package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.repository.ElectionRepository
import timber.log.Timber

class VoterInfoViewModel(
    private val repository: ElectionRepository,
    electionId: Int,
    division: Division
) : ViewModel() {

    init {
        Timber.e("ElectionId = $electionId")
        Timber.e("Division = $division")
    }

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}