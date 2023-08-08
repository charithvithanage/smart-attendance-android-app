package lnbti.charithgtp01.smartattendanceuserapp.interfaces

import android.location.Location

interface GetCurrentLocationListener {

    fun onLocationRead(location: Location)
    fun requestPermission()
    fun onError(error: String)
    fun openSettings()

}
