package lnbti.charithgtp01.smartattendanceuserapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.HiltAndroidApp
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils

@HiltAndroidApp
class UserApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        NetworkUtils.init(connectivityManager)
    }
}