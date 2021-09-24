package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.first
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.joinToString
import kotlin.collections.map
import kotlin.collections.toCollection

class DetailFragment : Fragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION_ID = 1001
    }

    private lateinit var toast: Toast
    private lateinit var binding: FragmentRepresentativeBinding
    private val permissions: Array<String> = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val locationManager: LocationManager by lazy {
        requireActivity().getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
    }

    private val locationListener = LocationListener { location ->
        updateViewsWithLocation(location)
    }

    private val statesArray: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.states)
    }

    //TODO: Declare ViewModel
    private lateinit var viewModel: RepresentativeViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        //TODO: Establish bindings

        // Disables the animation while the recyclerview is not filled
        binding.motionLayout.getTransition(R.id.representative_transition).setEnable(false)

        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)

        val listener = RepresentativeListener { representative ->
            Timber.e(representative.toString())
            Timber.e("TODO implement RepresentativeListener!!")
        }
        val adapter = RepresentativeListAdapter(listener)
        binding.representativeList.adapter = adapter

        binding.buttonLocation.setOnClickListener {
            if (checkLocationPermissions()) {
                getLocation()
            }
        }

        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            onFindMyRepresentativesClick()
        }

        viewModel.representatives.observe(viewLifecycleOwner, Observer { representatives ->
            representatives?.let {
                Timber.e("Representatives = ${it.joinToString("\n")}")
                adapter.submitList(it)
                binding.motionLayout.getTransition(R.id.representative_transition).setEnable(true)
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    binding.loadingImg.visibility = View.VISIBLE
                } else {
                    binding.loadingImg.visibility = View.INVISIBLE
                }
            }
        })

        viewModel.toast.observe(viewLifecycleOwner, Observer { id ->
            id?.let {
                showToast(it)
                viewModel.onToastShown()
            }
        })

        //TODO: Define and assign Representative adapter

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
        if (requestCode == REQUEST_LOCATION_PERMISSION_ID) {
            val missingPermissions = permissions.toCollection(ArrayList())
            permissions.forEach { permission ->
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) == PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.remove(permission)
                }
            }

            if (missingPermissions.isNotEmpty()) {
                Timber.e("The following permissions were not granted: ${missingPermissions.joinToString(",")}")
                Snackbar.make(
                    binding.root,
                    getString(R.string.missing_location_permissions),
                    Snackbar.LENGTH_LONG
                )
                    // Extracted from: https://github.com/udacity/android-kotlin-geo-fences/blob/master/app/src/main/java/com/example/android/treasureHunt/HuntMainActivity.kt#L145
                    .setAction(R.string.settings) {
                        // Displays App settings screen.
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()
            } else {
                getLocation()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(
                permissions, REQUEST_LOCATION_PERMISSION_ID
            )
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        permissions.forEach { permission ->
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        val location: Location? =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            updateViewsWithLocation(location)
        } else {
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_LOW_POWER
            }
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val settingsClient = LocationServices.getSettingsClient(requireActivity())
            val locationSettingsResponseTask =
                settingsClient.checkLocationSettings(builder.build())
            locationSettingsResponseTask.addOnSuccessListener {
                val minTime = 5000L
                val minDistance = 50f
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    locationListener
                )
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun updateViewsWithLocation(location: Location) {
        try {
            val address = geoCodeLocation(location)
            binding.addressLine1.setText(address.line1)
            binding.addressLine2.setText(address.line2)
            binding.city.setText(address.city)
            binding.zip.setText(address.zip)
            binding.state.setNewValue(address.state)
            viewModel.getRepresentatives(address)
        } catch (e: IOException) {
            Timber.e(e)
            showToast(e.message ?: "An IOException when trying to get the device's location!!")
        }
    }

    private fun onFindMyRepresentativesClick() {
        val state = statesArray[binding.state.selectedItemId.toInt()]
        val address = Address(
            binding.addressLine1.text.toString(),
            binding.addressLine2.text.toString(),
            binding.city.text.toString(),
            state,
            binding.zip.text.toString()
        )
        viewModel.getRepresentatives(address)
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun showToast(id: Int) {
        if (::toast.isInitialized) {
            // Cancels the current toast to avoid queueing multiple toasts
            toast.cancel()
        }
        toast = Toast.makeText(context, id, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun showToast(msg: String) {
        if (::toast.isInitialized) {
            // Cancels the current toast to avoid queueing multiple toasts
            toast.cancel()
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.show()
    }
}