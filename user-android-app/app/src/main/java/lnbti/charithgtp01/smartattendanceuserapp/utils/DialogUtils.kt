package lnbti.charithgtp01.smartattendanceuserapp.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.InputType
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.PROGRESS_DIALOG_FRAGMENT_TAG
import lnbti.charithgtp01.smartattendanceuserapp.dialogs.CustomAlertDialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.dialogs.CustomConfirmAlertDialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.dialogs.CustomProgressDialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ValueSubmitDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.changeUiSize

/**
 * Utils class for Dialogs and Alerts
 */
class DialogUtils {
    companion object {


        /**
         * Enter value and get the value to out side from dialog
         * @param context Activity
         * @param title Title of the dialog
         * @param hint Hint of the Input Text
         * @param valueSubmitDialogListener Dialog value listener
         */
        fun valueSubmitDialog(
            context: Activity,
            title: String,
            hint: String,
            valueSubmitDialogListener: ValueSubmitDialogListener
        ) {
            val dialog = Dialog(context, R.style.Theme_SmartAttendanceAdminApp_DialogNoActionBar)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.value_submit_dialog_layout)
            changeUiSize(context, dialog.findViewById(R.id.dialogMainLayout), 1, 1, 30)
            dialog.setCancelable(false)
            val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
            val inputText = dialog.findViewById<TextInputLayout>(R.id.inputText)
            val editText = dialog.findViewById<TextInputEditText>(R.id.editText)
            val btnOK = dialog.findViewById<Button>(R.id.btnOK)
            val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

            tvTitle.text = title
            inputText.hint = hint
            editText.inputType = InputType.TYPE_CLASS_NUMBER

            btnCancel.setOnClickListener { dialog.dismiss() }
            btnOK.setOnClickListener {
                dialog.dismiss()
                valueSubmitDialogListener.onPositiveButtonClicked(
                    editText.text.toString()
                )
            }
            dialog.show()
        }


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
                    CustomConfirmAlertDialogFragment.newInstance(message,  dialogButtonClickListener)
                dialogFragment.show(fragmentManager, "CustomConfirmDialogFragmentTag")
            }
        }

        /**
         * Progress Dialog Inside Activity
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
        fun showProgressDialogInFragment(context: Context?, message: String?): DialogFragment? {
            var dialogFragment: DialogFragment? = null
            if (context != null) {
                val fragmentManager = (context as? FragmentActivity)?.supportFragmentManager
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