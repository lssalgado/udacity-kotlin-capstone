package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION_ID = 1001
    }

    private lateinit var binding: FragmentRepresentativeBinding
    private val permissions: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )


    //TODO: Declare ViewModel
    private lateinit var viewModel: RepresentativeViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        //TODO: Establish bindings

        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)

        val listener = RepresentativeListener { representative ->
            Timber.e(representative.toString())
            Timber.e("TODO implement RepresentativeListener!!")
        }
        val adapter = RepresentativeListAdapter(listener)
        binding.representativeList.adapter = adapter

        binding.buttonLocation.setOnClickListener {
            checkLocationPermissions()
        }

        viewModel.representatives.observe(viewLifecycleOwner, Observer { representatives ->
            representatives?.let {
                Timber.e("Representatives = ${it.joinToString("\n")}")
                adapter.submitList(it)
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

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}