package lnbti.charithgtp01.smartattendanceuserapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.GetCurrentLocationListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.LOCATION_PERMISSION_REQUEST_CODE
import java.util.*

class LocationActivity : AppCompatActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var tvCountryName: TextView
    private lateinit var tvLocality: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnSearch: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        tvLatitude = findViewById(R.id.tv_latitude)
        tvLongitude = findViewById(R.id.tv_longitude)
        tvCountryName = findViewById(R.id.tv_countryName)
        tvLocality = findViewById(R.id.tv_Locality)
        tvAddress = findViewById(R.id.tv_address)
        btnSearch = findViewById(R.id.btn_location)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkPermissions(this)) {
            if (isLocationEnabled(this)) {
                getCurrentLocation(this, mFusedLocationClient, object : GetCurrentLocationListener {
                    override fun onLocationRead(location: Location) {
                        if (location != null) {
                            tvLatitude.text = "Latitude\n${location.latitude}"
                            tvLongitude.text = "Longitude\n${location.latitude}"
                        }
                    }

                    override fun requestPermission() {
                        TODO("Not yet implemented")
                    }

                    override fun onError(error: String) {
                        TODO("Not yet implemented")
                    }

                    override fun openSettings() {
                        TODO("Not yet implemented")
                    }

                })
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions(this)
        }

//        getLocation()
    }

    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    fun checkPermissions(activity: Activity): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun isLocationEnabled(activity: Activity): Boolean {
        val locationManager: LocationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermission()) {
            if (locationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        tvLatitude.text = "Latitude\n${location.latitude}"
                        tvLongitude.text = "Longitude\n${location.latitude}"
//                        val geocoder = Geocoder(this, Locale.getDefault())
//                        val list: List<Address> =
//                            geocoder.getFromLocation(location.latitude, location.longitude, 1) as List<Address>
//                        tvLatitude.text = "Latitude\n${list[0].latitude}"
//                        tvLongitude.text = "Longitude\n${list[0].longitude}"
//                        tvCountryName.text = "Country Name\n${list[0].countryName}"
//                        tvLocality.text = "Locality\n${list[0].locality}"
//                        tvAddress.text = "Address\n${list[0].getAddressLine(0)}"
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun locationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

//    @SuppressLint("MissingSuperCall")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == permissionId) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                getLocation()
//            }
//        }
//    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(
                    applicationContext,
                    "GPS location permission granted", Toast.LENGTH_SHORT
                )
            }
        }
    }
    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getCurrentLocation(
        activity: Activity,
        mFusedLocationClient: FusedLocationProviderClient,
        listener: GetCurrentLocationListener
    ) {
        if (checkPermissions(this)) {
            if (isLocationEnabled(activity)) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        listener.onLocationRead(location)
                    }
                }
            } else {
                listener.openSettings()
            }
        } else {
            listener.requestPermission()
        }
    }
}