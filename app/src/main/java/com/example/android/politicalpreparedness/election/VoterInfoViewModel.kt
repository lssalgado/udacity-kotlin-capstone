package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(
    private val repository: ElectionRepository,
    private val electionId: Int,
    val division: Division
) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    val election = MediatorLiveData<Election>()

    init {
        Timber.e("ElectionId = $electionId")
        Timber.e("Division = $division")
        getVoterInfo()
        election.addSource(repository.getElectionById(electionId), election::setValue)
    }

    private fun getVoterInfo() {
        viewModelScope.launch {
            val voterInfo = CivicsApi.retrofitService.getVoterInfo(
                "${division.country}, ${division.state}",
                electionId,
                BuildConfig.API_KEY
            )
            Timber.e(voterInfo.toString())
            _voterInfo.value = voterInfo
            if (voterInfo.state != null && voterInfo.state.isNotEmpty()){
                _state.value = voterInfo.state[0]
            } else {
                _toastText.value = R.string.missing_state
            }
        }
    }

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs
    private val _urlToLoad = MutableLiveData<String>()
    val urlToLoad: LiveData<String>
        get() = _urlToLoad

    private val _toastText = MutableLiveData<Int>()
    val toastText: LiveData<Int>
        get() = _toastText

    fun onUrlClick(url: String) {
        if (url.isNotEmpty()) {
            _urlToLoad.value = url
        } else {
            _toastText.value = R.string.missing_url
        }
    }

    fun onUrlIntentStarted() {
        _urlToLoad.value = null
    }

    fun onToastShown() {
        _toastText.value = null
    }

    fun onFollowClick() {
        viewModelScope.launch {
            val currentElection = election.value
            if (currentElection != null) {
                val saved = currentElection.saved
                val old = currentElection.old
                // Is currently saved and is a past election
                if (old && saved) {
                    // Them it should be removed from the database
                    repository.deleteElection(currentElection.id)
                } else {
                    // Otherwise, just invert the current `saved` value
                    currentElection.saved = !saved
                    repository.updateElection(currentElection)
                }
            } else {
                _toastText.value = R.string.missing_election
            }
        }

    }
}