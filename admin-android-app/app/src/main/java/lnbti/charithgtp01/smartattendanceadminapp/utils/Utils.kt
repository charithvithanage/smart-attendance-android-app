package lnbti.charithgtp01.smartattendanceadminapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.SuccessListener

/**
 * A Utils class containing Common Methods
 */
class Utils {
    companion object {

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