package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel: ViewModel() {


    init {
        getRepresentatives()
    }

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

    private fun getRepresentatives() {
        viewModelScope.launch {
            val (offices, officials) = CivicsApi.retrofitService.getRepresentatives("3601+s+broad+st+philadelphia+pa+19148%2C+united+states")
            Timber.e(offices.toString())
            Timber.e(officials.toString())
            _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
            representatives.value!!.forEach {
                Timber.e(it.toString())
            }
        }
    }
    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
