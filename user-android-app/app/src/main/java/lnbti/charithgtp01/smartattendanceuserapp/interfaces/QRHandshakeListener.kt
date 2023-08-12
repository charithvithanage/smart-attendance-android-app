package lnbti.charithgtp01.smartattendanceuserapp.interfaces

import android.location.Location

interface QRHandshakeListener {

    fun onSuccess(location: Location)
    fun onError(error: String)

}
