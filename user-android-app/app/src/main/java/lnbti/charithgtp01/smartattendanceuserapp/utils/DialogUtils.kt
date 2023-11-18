package lnbti.charithgtp01.smartattendanceuserapp.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.ALERT_DIALOG_FRAGMENT_TAG
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.CONFIRM_DIALOG_FRAGMENT_TAG
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.PROGRESS_DIALOG_FRAGMENT_TAG
import lnbti.charithgtp01.smartattendanceuserapp.dialogs.CustomAlertDialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.dialogs.CustomConfirmAlertDialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.dialogs.CustomProgressDialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener

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
            (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomAlertDialogFragment.newInstance(message, type, dialogButtonClickListener)
                    .show(
                        fragmentManager,
                        ALERT_DIALOG_FRAGMENT_TAG
                    )
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
            (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomAlertDialogFragment.newInstance(message)
                    .show(
                        fragmentManager,
                        ALERT_DIALOG_FRAGMENT_TAG
                    )
            }
        }

        fun showErrorDialogInFragment(
            fragment: Fragment, message: String?
        ) {
            fragment?.parentFragmentManager?.let { fragmentManager ->
                CustomAlertDialogFragment.newInstance(message).apply {
                    show(fragmentManager, ALERT_DIALOG_FRAGMENT_TAG)
                }
            }
        }

        /**
         * Show a custom confirm alert dialog with an icon inside an activity.
         *
         * @param context The context in which the dialog should be shown.
         * @param message The message body to be displayed in the dialog.
         * @param dialogButtonClickListener The listener for dialog button click events.
         */
        fun showConfirmAlertDialog(
            context: Context,
            message: String?,
            dialogButtonClickListener: ConfirmDialogButtonClickListener
        ) {

            (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomConfirmAlertDialogFragment.newInstance(message, dialogButtonClickListener)
                    .show(
                        fragmentManager,
                        CONFIRM_DIALOG_FRAGMENT_TAG
                    )
            }
        }

        /**
         * Show a progress dialog inside a fragment.
         *
         * @param activity The fragment in which the progress dialog should be shown.
         * @param message The progress message to be displayed.
         * @return The created progress dialog fragment.
         */
        fun showProgressDialog(context: Context?, message: String?): DialogFragment? {
            return (context as? AppCompatActivity)?.supportFragmentManager?.let { fragmentManager ->
                CustomProgressDialogFragment.newInstance(message).apply {
                    show(fragmentManager, PROGRESS_DIALOG_FRAGMENT_TAG)
                }
            }
        }

        /**
         * Show a progress dialog inside a fragment.
         *
         * @param activity The fragment in which the progress dialog should be shown.
         * @param message The progress message to be displayed.
         * @return The created progress dialog fragment.
         */
        fun showProgressDialogInFragment(fragment: Fragment?, message: String?): DialogFragment? {
            return fragment?.parentFragmentManager?.let { fragmentManager ->
                CustomProgressDialogFragment.newInstance(message).apply {
                    show(fragmentManager, PROGRESS_DIALOG_FRAGMENT_TAG)
                }
            }
        }
    }

}