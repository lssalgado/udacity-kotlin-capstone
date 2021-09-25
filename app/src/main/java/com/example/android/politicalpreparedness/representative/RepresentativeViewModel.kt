package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.network.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class RepresentativeViewModel : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val address = MutableLiveData<Address>(Address("", "", "", "", ""))

    private val _toast = MutableLiveData<Int>()
    val toast: LiveData<Int>
        get() = _toast

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result>
        get() = _result

    fun getRepresentatives(inputAddress: Address? = address.value) {
        address.value = inputAddress
        address.value?.let { address ->
            when {
                address.line1.isBlank() -> {
                    _toast.value = R.string.form_missing_street
                }
                address.line2.isNullOrBlank() -> {
                    _toast.value = R.string.form_missing_street_number
                }
                address.city.isBlank() -> {
                    _toast.value = R.string.form_missing_city
                }
                address.state.isBlank() -> {
                    _toast.value = R.string.form_missing_state
                }
                address.zip.isBlank() -> {
                    _toast.value = R.string.form_missing_zip
                }
                else -> {
                    _loading.value = true
                    viewModelScope.launch {
                        try {
                            val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
                            _loading.value = false
                            _representatives.value =
                                offices.flatMap { office -> office.getRepresentatives(officials) }
                        } catch (e: HttpException) {
                            Timber.e(e)
                            _result.value = Result.HttpError(e.code())
                            _loading.value = false
                        } catch (e: Exception) {
                            Timber.e(e)
                            _result.value =
                                Result.Error(e.message ?: "Could not fetch Representatives from the API!!")
                            _loading.value = false
                        }
                    }
                }
            }
            return
        }
        _toast.value = R.string.form_missing_street
    }

    fun onToastShown() {
        _toast.value = null
    }

    fun onResultHandled() {
        _result.value = null
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
