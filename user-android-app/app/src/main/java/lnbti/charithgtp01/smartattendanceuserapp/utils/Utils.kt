package lnbti.charithgtp01.smartattendanceuserapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import lnbti.charithgtp01.smartattendanceuserapp.MainActivity
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.SuccessListener
import java.util.Calendar
import java.util.Date

/**
 * A Utils class containing Common Methods
 */
class Utils {
    companion object {

        const val LOCATION_PERMISSION_REQUEST_CODE: Int = 100



        /**
         * Last Day of the month for given date
         */
        fun getLastDayOfMonth(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            return calendar.time
        }


        /**
         * Request Location Permission
         */
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

        /**
         * Check Location Permission Granted or Not
         */
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

        /**
         * Check permissions
         *
         * @param context
         * @param permissions
         * @return
         */
        fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
            if (context != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            permission
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }
            return true
        }

        /**
         * Generate Device Unique Identifier
         */
        @SuppressLint("HardwareIds")
        fun getAndroidId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        /**
         * Check Internet Status
         */
        fun isOnline(context: Context?): Boolean {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }

        /**
         * Navigate to Home Activity with Clearing all top activities
         */
        fun goToHomeActivity(context: Context) {
            navigateWithoutHistory(context, MainActivity::class.java)
        }


        /**
         * Navigate to another activity without navigation history
         *
         * @param context  Context of the current activity
         * @param activity Context of the second activity
         */
        fun navigateWithoutHistory(context: Context, activity: Class<*>?) {
            val intent = Intent(context, activity)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        /**
         * Navigate to another activity
         */
        fun navigateToAnotherActivity(context: Context, activity: Class<*>?) {
            val intent = Intent(context, activity)
            context.startActivity(intent)
        }

        /**
         * Navigate to another activity with data extras
         * Activity wait for a Result
         */
        fun navigateToAnotherActivityForResultWithExtras(
            context: Activity,
            activity: Class<*>?,
            requestCode: Int,
            map: java.util.HashMap<String, String>
        ) {
            val intent = Intent(context, activity)

            map.keys.forEach { key ->
                val value = map[key]
                intent.putExtra(key, value)
            }
            context.startActivityForResult(intent, requestCode)
        }

        /**
         * Navigate to another activity with data extras
         */
        fun navigateToAnotherActivityWithExtras(
            context: Context,
            activity: Class<*>?,
            map: java.util.HashMap<String, String>
        ) {
            val intent = Intent(context, activity)
            map.keys.forEach { key ->
                val value = map[key]
                intent.putExtra(key, value)
            }
            context.startActivity(intent)
        }

        /**
         * Navigate to another activity with data extras
         * Clear all previous Activities
         */
        fun navigateToAnotherActivityWithExtrasWithoutHistory(
            context: Context,
            activity: Class<*>?,
            map: java.util.HashMap<String, String>
        ) {
            val intent = Intent(context, activity)
            map.keys.forEach { key ->
                val value = map[key]
                intent.putExtra(key, value)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        /**
         * Get preference value from key value
         */
        fun getObjectFromSharedPref(
            context: Context,
            key: String?
        ): String? {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
            return sharedPref.getString(key, null)
        }

        /**
         * Save preference value with key
         */
        fun saveObjectInSharedPref(
            context: Context,
            key: String?,
            jsonString: String?,
            listener: SuccessListener
        ) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            editor.putString(key, jsonString)
            editor.apply()
            listener.onFinished()
        }

        /**
         * Save multiple preference values with multiple keys
         */
        fun saveMultipleObjectsInSharedPref(
            context: Context,
            map: HashMap<String, String>,
            listener: SuccessListener
        ) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()

            map.keys.forEach { key ->
                val value = map[key]
                editor.putString(key, value)
            }

            editor.apply()
            listener.onFinished()
        }

    }
}