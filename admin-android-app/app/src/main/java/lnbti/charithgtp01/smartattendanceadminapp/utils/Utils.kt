package lnbti.charithgtp01.smartattendanceadminapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lnbti.charithgtp01.smartattendanceadminapp.MainActivity
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.SuccessListener
import lnbti.charithgtp01.smartattendanceadminapp.model.ErrorBody
import okhttp3.ResponseBody

/**
 * A Utils class containing Common Methods
 */
class Utils {
    companion object {

        val userRoles:  MutableList<String> = mutableListOf("Office User", "Employee")
        val userTypes:  MutableList<String> = mutableListOf("Android User", "Other")

        /**
         * Deserialize error response.body
         * @param errorBody Error Response
         */
        fun getErrorBodyFromResponse(errorBody: ResponseBody?): ErrorBody {
            Log.d(Constants.TAG, errorBody.toString())
            val gson = Gson()
            val type = object : TypeToken<ErrorBody>() {}.type
            return gson.fromJson(errorBody?.charStream(), type)
        }

        /**
         * Check Internet Status
         */
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

        /**
         * Clear all preference values
         */
        fun clearAllPref(context: Context, listener: SuccessListener) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            sharedPref.edit().clear().apply()
            listener.onFinished()
        }

    }
}