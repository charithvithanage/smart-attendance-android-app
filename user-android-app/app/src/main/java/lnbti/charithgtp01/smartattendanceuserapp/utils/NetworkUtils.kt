package lnbti.charithgtp01.smartattendanceuserapp.utils

import android.net.ConnectivityManager
import javax.inject.Inject

/**
 * Kotlin object that manages the connectivity check.
 */
class NetworkUtils @Inject constructor() {
    companion object {
        private var connectivityManager: ConnectivityManager? = null

        /**
         * Call the NetworkUtils.init() method to set the ConnectivityManager
         */
        fun init(connectivityManager: ConnectivityManager) {
            this.connectivityManager = connectivityManager
        }

        /**
         * can use this method from anywhere in the app to check for network
         * connectivity without passing a Context
         */
        fun isNetworkAvailable(): Boolean {
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }


}