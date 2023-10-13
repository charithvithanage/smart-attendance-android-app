package lnbti.charithgtp01.smartattendanceadminapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.HiltAndroidApp
import lnbti.charithgtp01.smartattendanceadminapp.utils.NetworkUtils

@HiltAndroidApp
class AdminApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        NetworkUtils.init(connectivityManager)
    }
}