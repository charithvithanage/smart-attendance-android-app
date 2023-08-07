package lnbti.charithgtp01.smartattendanceadminapp.utils

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.dialogs.CustomAlertDialogFragment
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.changeUiSize

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
                val dialogFragment = CustomAlertDialogFragment.newInstance(message, type,dialogButtonClickListener)
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
            Handler(Looper.getMainLooper()).post {
                Dialog(context, R.style.DialogNoActionBar).apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    setCancelable(false)
                    setContentView(R.layout.confirm_alert_dialog_layout)
                    changeUiSize(context, findViewById(R.id.dialogMainLayout), 1, 1, 30)
                    changeUiSize(context, findViewById(R.id.icon), 1, 3)
                    val tvMessage = findViewById<TextView>(R.id.tvMessage)
                    val icon = findViewById<ImageView>(R.id.icon)
                    val buttonYes =
                        findViewById<Button>(R.id.buttonYes)
                    val buttonNo =
                        findViewById<Button>(R.id.buttonNo)

                    buttonYes.setOnClickListener {
                        dismiss()
                        dialogButtonClickListener.onPositiveButtonClick()
                    }

                    buttonNo.setOnClickListener {
                        dismiss()
                        dialogButtonClickListener.onNegativeButtonClick()
                    }
                    tvMessage.text = message
                    icon.setImageResource(R.mipmap.question_mark)

                    show()
                }
            }
        }


        /**
         * Custom Alert Dialog with icon
         * @param message Message body
         */
//        fun showAlertDialog(
//            context: Activity,
//            message: String?,
//            dialogButtonClickListener: DialogButtonClickListener
//        ) {
//
//            Handler(Looper.getMainLooper()).post {
//                Dialog(context, R.style.DialogNoActionBar).apply {
//                    requestWindowFeature(Window.FEATURE_NO_TITLE)
//                    setCancelable(false)
//                    setContentView(R.layout.alert_dialog_layout)
//                    changeUiSize(context, findViewById(R.id.dialogMainLayout), 1, 1, 30)
//                    changeUiSize(context, findViewById(R.id.icon), 1, 3)
//                    val tvMessage = findViewById<TextView>(R.id.tvMessage)
//                    val icon = findViewById<ImageView>(R.id.icon)
//                    val button =
//                        findViewById<Button>(R.id.button)
//
//                    button.setOnClickListener {
//                        dismiss()
//                        dialogButtonClickListener.onButtonClick()
//                    }
//                    tvMessage.text = message
//                    icon.setImageResource(R.mipmap.done)
//
//                    show()
//                }
//            }
//        }

        /**
         * Error Dialog with icon
         * @param error Error Message
         */
//        fun showErrorDialog(
//            context: Context,
//            error: String?,
//            errorDialogButtonClickListener: DialogButtonClickListener
//        ) {
//            Handler(Looper.getMainLooper()).post {
//                Dialog(context, R.style.DialogNoActionBar).apply {
//                    requestWindowFeature(Window.FEATURE_NO_TITLE)
//                    setCancelable(false)
////                    setContentView(R.layout.alert_dialog_layout)
//                    changeUiSize(context, findViewById(R.id.dialogMainLayout), 1, 1, 30)
//                    changeUiSize(context, findViewById(R.id.icon), 1, 3)
//                    val tvMessage = findViewById<TextView>(R.id.tvMessage)
//                    val icon = findViewById<ImageView>(R.id.icon)
//                    val button =
//                        findViewById<Button>(R.id.button)
//
//                    button.setOnClickListener {
//                        dismiss()
//                        errorDialogButtonClickListener.onButtonClick()
//                    }
//                    tvMessage.text = error
//                    icon.setImageResource(R.mipmap.cancel)
//                    show()
//                }
//            }
//        }

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