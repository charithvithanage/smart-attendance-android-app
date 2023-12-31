package lnbti.charithgtp01.smartattendanceuserapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import lnbti.charithgtp01.smartattendanceuserapp.ui.main.MainActivity
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.BIO_METRIC_ENABLE_STATUS
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.LAST_LOGGED_IN_CREDENTIAL
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.GetCurrentLocationListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.SuccessListener
import lnbti.charithgtp01.smartattendanceuserapp.model.ErrorBody
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A Utils class containing Common Methods
 */
class Utils {
    companion object {
        /**
         * Parse the string containing the list into a user object list
         */
        fun parseStringToUserList(stringData: String?): List<User> {
            val gson = Gson()
            val listType = object : TypeToken<List<User>>() {}.type
            return gson.fromJson(stringData, listType)
        }

        /**
         * Deserialize error response.body
         * @param errorBody Error Response
         */
        fun getErrorBodyFromResponse(errorBody: ResponseBody?): ErrorBody {
            Log.d(TAG, errorBody.toString())
            val gson = Gson()
            val type = object : TypeToken<ErrorBody>() {}.type
            return gson.fromJson(errorBody?.charStream(), type)
        }


        /**
         * Format date to string
         */
        fun formatDate(date: Date): String {
            val format = SimpleDateFormat(
                "dd.MM.yyyy",
                Locale.getDefault()
            )
            return format.format(date)
        }

       /**
         * Format time to string
         */
        fun formatTime(date: Date): String {
            val format = SimpleDateFormat(
                "hh:mm:ss a",
                Locale.getDefault()
            )
            return format.format(date)
        }

        fun formatDateWithMonth(inputDate: String): String {
            val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

            val date: Date = inputFormat.parse(inputDate) ?: Date()
            return outputFormat.format(date)
        }


        /**
         * @param dateString Date as String
         * @return Day of the week (Monday,Tuesday,...)
         */
        fun formatDateToDayOfWeek(dateString: String): String {
            // Define the input date format
            val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)

            return try {
                // Parse the input date string
                val date = inputFormat.parse(dateString)

                // Define the output date format for the day of the week
                val outputFormat = SimpleDateFormat("EEEE", Locale.US)

                // Format the date to get the day of the week
                outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                "Invalid Date"
            }
        }

        fun formatTodayDate(context: Context): String {
            val calendar = Calendar.getInstance()
            val dateFormat =
                SimpleDateFormat(context.getString(R.string.date_format), Locale.getDefault())
            return dateFormat.format(calendar.time)
        }

        const val LOCATION_PERMISSION_REQUEST_CODE: Int = 100
        private const val areaRadius = 100.0 // in meters

        /**
         * Get Current Location Method
         */
        @SuppressLint("MissingPermission", "SetTextI18n")
        fun getCurrentLocation(
            activity: Activity,
            mFusedLocationClient: FusedLocationProviderClient,
            listener: GetCurrentLocationListener
        ) {
            if (checkPermissions(activity)) {
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

        /**
         * Check the given latitude and longitude is inside any given area
         */
        fun isLocationCorrect(
            scannedLatitude: Double,
            scannedLongitude: Double,
            areaLatitude: Double,
            areaLongitude: Double
        ): Boolean {
            // Check if the current location is inside the desired area
            val distance = calculateDistance(
                scannedLatitude,
                scannedLongitude,
                areaLatitude,
                areaLongitude
            )
            return if (distance <= areaRadius) {
                // Inside the area
                // Perform your desired actions here
                Log.d(TAG, "Inside Area")
                true
            } else {
                // Outside the area
                // Perform your desired actions here
                Log.d(TAG, "Outside Area")
                false
            }
        }

        // Helper method to calculate distance between two coordinates
        private fun calculateDistance(
            currentLatitude: Double,
            currentLongitude: Double,
            areaLatitude: Double,
            areaLongitude: Double
        ): Float {
            val results = FloatArray(1)
            Location.distanceBetween(
                areaLatitude,
                areaLongitude,
                currentLatitude,
                currentLongitude,
                results
            )
            return results[0]
        }


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

        /**
         * Clear all preference values
         */
        private fun clearAllPref(context: Context, listener: SuccessListener) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            sharedPref.edit().clear().apply()
            listener.onFinished()
        }

        fun logout(context: Context, listener: SuccessListener) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )

            val savedPref1 = sharedPref.getString(LAST_LOGGED_IN_CREDENTIAL, null)
            val savedPref2 = sharedPref.getBoolean(BIO_METRIC_ENABLE_STATUS, false)

            clearAllPref(
                context
            ) {
                val editor = sharedPref.edit()
                editor.putString(LAST_LOGGED_IN_CREDENTIAL, savedPref1)
                editor.putBoolean(BIO_METRIC_ENABLE_STATUS, savedPref2)
                editor.apply()
                listener.onFinished()
            }
        }

        /**
         * Converts the provided [text] into a QR code represented as a [Bitmap].
         *
         * @param text The value to be encoded into the QR code.
         * @return The generated QR code as a [Bitmap].
         */
        fun generateQRCodeBitmap(text: String): Bitmap {
            try {
                QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, 512, 512).apply {
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bitmap.setPixel(x, y, if (get(x, y)) Color.BLACK else Color.WHITE)
                        }
                    }
                    return bitmap
                }
            } catch (e: WriterException) {
                // Handle the exception if QR code generation fails
                e.printStackTrace()
                return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
            }
        }
    }
}