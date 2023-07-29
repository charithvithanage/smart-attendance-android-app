package lnbti.charithgtp01.smartattendanceadminapp.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
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
         */
        fun showAlertDialog(
            context: Context,
            message: String?,
            dialogButtonClickListener: DialogButtonClickListener
        ) {
            Handler(Looper.getMainLooper()).post {
                Dialog(context, R.style.DialogNoActionBar).apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(false)
                    setContentView(R.layout.alert_dialog_layout)
                    changeUiSize(context, findViewById(R.id.dialogMainLayout), 1, 1, 30)
                    changeUiSize(context, findViewById(R.id.icon), 1, 3)
                    val tvMessage = findViewById<TextView>(R.id.tvMessage)
                    val icon = findViewById<ImageView>(R.id.icon)
                    val button =
                        findViewById<Button>(R.id.button)

                    button.setOnClickListener {
                        dismiss()
                        dialogButtonClickListener.onButtonClick()
                    }
                    tvMessage.text = message
                    icon.setImageResource(R.mipmap.done)

                    show()
                }
            }
        }

        /**
         * Error Dialog with icon
         * @param error Error Message
         */
        fun showErrorDialog(
            context: Context,
            error: String?,
            errorDialogButtonClickListener: DialogButtonClickListener
        ) {
            Handler(Looper.getMainLooper()).post {
                Dialog(context, R.style.DialogNoActionBar).apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(false)
                    setContentView(R.layout.alert_dialog_layout)
                    changeUiSize(context, findViewById(R.id.dialogMainLayout), 1, 1, 30)
                    changeUiSize(context, findViewById(R.id.icon), 1, 3)
                    val tvMessage = findViewById<TextView>(R.id.tvMessage)
                    val icon = findViewById<ImageView>(R.id.icon)
                    val button =
                        findViewById<Button>(R.id.button)

                    button.setOnClickListener {
                        dismiss()
                        errorDialogButtonClickListener.onButtonClick()
                    }
                    tvMessage.text = error
                    icon.setImageResource(R.mipmap.cancel)
                    show()
                }
            }
        }

        /**
         * Progress Dialog
         * @param message progress message
         */
        fun showProgressDialog(context: Context?, message: String?): Dialog? {
            var dialog: Dialog? = null
            if (context != null) {
                dialog = Dialog(context, R.style.DialogNoActionBar).apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(false)
                    setContentView(R.layout.progress_dialog_layout)
                    changeUiSize(context, findViewById(R.id.dialogMainLayout), 1, 1, 30)
                    changeUiSize(context, findViewById(R.id.icon), 1, 3)
                    val tvMessage = findViewById<TextView>(R.id.tvMessage)
                    tvMessage.text = message
                }
            }
            return dialog
        }
    }

}