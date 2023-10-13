package lnbti.charithgtp01.smartattendanceadminapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.PROGRESS_DIALOG_FRAGMENT_TAG
import lnbti.charithgtp01.smartattendanceadminapp.dialogs.CustomAlertDialogFragment
import lnbti.charithgtp01.smartattendanceadminapp.dialogs.CustomConfirmAlertDialogFragment
import lnbti.charithgtp01.smartattendanceadminapp.dialogs.CustomProgressDialogFragment
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener

/**
 * Utils class for Dialogs and Alerts
 */
class DialogUtils {
    companion object {

        /**
         * Custom Alert Dialog with icon
         * @param message Message body
         * @param type Type of the Dialog Success,Fail or Warn Alert
         */
        fun showAlertDialog(
            context: Context, type: String, message: String?,
            dialogButtonClickListener: CustomAlertDialogListener
        ) {
            val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
            if (fragmentManager != null) {
                val dialogFragment =
                    CustomAlertDialogFragment.newInstance(message, type, dialogButtonClickListener)
                dialogFragment.show(fragmentManager, "CustomDialogFragmentTag")
            }
        }

        /**
         * Custom Alert Dialog with icon
         * @param message Message body
         * @param type Type of the Dialog Success,Fail or Warn Alert
         */
        fun showErrorDialog(
            context: Context, message: String?
        ) {
            val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
            if (fragmentManager != null) {
                val dialogFragment = CustomAlertDialogFragment.newInstance(message)
                dialogFragment.show(fragmentManager, "CustomDialogFragmentTag")
            }
        }

        fun showErrorDialogInFragment(
            fragment: Fragment, message: String?
        ) {
            val fragmentManager = fragment.fragmentManager
            if (fragmentManager != null) {
                val dialogFragment = CustomAlertDialogFragment.newInstance(message)
                dialogFragment.show(fragmentManager, "CustomDialogFragmentTag")
            }
        }

        /**
         * Custom Confirm Alert Dialog with icon
         * @param message Message body
         * @param dialogButtonClickListener Dialog Button Click event listener
         *
         */
        fun showConfirmAlertDialog(
            context: Context,
            message: String?,
            dialogButtonClickListener: ConfirmDialogButtonClickListener
        ) {
            val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
            if (fragmentManager != null) {
                val dialogFragment =
                    CustomConfirmAlertDialogFragment.newInstance(message, dialogButtonClickListener)
                dialogFragment.show(fragmentManager, "CustomConfirmDialogFragmentTag")
            }

        }

        /**
         * Progress Dialog
         * @param message progress message
         */
        fun showProgressDialog(context: Context?, message: String?): DialogFragment? {
            var dialogFragment: DialogFragment? = null
            if (context != null) {
                val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
                if (fragmentManager != null) {
                    dialogFragment =
                        CustomProgressDialogFragment.newInstance(message)
                    dialogFragment.show(fragmentManager, PROGRESS_DIALOG_FRAGMENT_TAG)
                }
            }
            return dialogFragment
        }

        /**
         * Progress Dialog in Fragment
         * @param message progress message
         */
        fun showProgressDialogInFragment(activity: Fragment, message: String?): DialogFragment? {
            var dialogFragment: DialogFragment? = null
            if (activity != null) {
                val fragmentManager = activity.fragmentManager
                if (fragmentManager != null) {
                    dialogFragment =
                        CustomProgressDialogFragment.newInstance(message)
                    dialogFragment.show(fragmentManager, PROGRESS_DIALOG_FRAGMENT_TAG)
                }
            }
            return dialogFragment
        }
    }

}