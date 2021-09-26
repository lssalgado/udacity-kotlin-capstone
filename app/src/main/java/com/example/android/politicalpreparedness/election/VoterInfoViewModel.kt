package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import com.example.android.politicalpreparedness.network.Result

class VoterInfoViewModel(
    private val repository: ElectionRepository,
    private val electionId: Int,
    private val division: Division
) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    val election = MediatorLiveData<Election>()

    init {
        election.addSource(repository.getElectionById(electionId), election::setValue)
    }

    fun getVoterInfo() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val voterInfo = CivicsApi.retrofitService.getVoterInfo(
                    "${division.country}, ${division.state}",
                    electionId
                )
                _voterInfo.value = voterInfo
                if (voterInfo.state != null && voterInfo.state.isNotEmpty()) {
                    _state.value = voterInfo.state[0]
                } else {
                    _toastText.value = R.string.missing_state
                }
            } catch (e: HttpException) {
                Timber.e(e)
                _result.value = Result.HttpError(e.code())
            } catch (e: Exception) {
                Timber.e(e)
                _result.value = Result.Error(e.message ?: "Could not fetch VoterInfo from the API!!")
            } finally {
                _loading.value = false
            }
        }
    }

    private val _urlToLoad = MutableLiveData<String>()
    val urlToLoad: LiveData<String>
        get() = _urlToLoad

    private val _toastText = MutableLiveData<Int>()
    val toastText: LiveData<Int>
        get() = _toastText

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result>
        get() = _result

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

    fun onResultHandled() {
        _result.value = null
    }

    fun onLoadingHandled() {
        _loading.value = null
    }
}