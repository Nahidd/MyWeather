package com.nahid.weather

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.nahid.weather.network.detectUserLocation
import com.nahid.weather.viewmodel.LocationViewModel

class MainActivity : AppCompatActivity() {
    private val locationViewModel: LocationViewModel by viewModels()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getUserLocation()
                //Toast.makeText(this, "fine location granted", Toast.LENGTH_SHORT).show()
               
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getUserLocation()
                //Toast.makeText(this, "coarse location granted", Toast.LENGTH_SHORT).show()
                // Only approximate location access granted.
            } else -> {

            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
            // No location access granted.
        }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }
   @SuppressLint("MissingPermission")
    private fun getUserLocation(){
        detectUserLocation(this) {
            locationViewModel.setNewLocationLiveData(it)
        }

    }
}

/*fun isLocationPermissionGranted(context: Context) : Boolean {
    return ContextCompat
        .checkSelfPermission(context,
        android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context,
        android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

}


fun requestUserForLocationPermission(activity: Activity) {
    activity.requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION),
        111)

}
*/



