package lnbti.charithgtp01.smartattendanceadminapp.utils

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.InputTextListener

class UIUtils {

    companion object {
        fun normalState(inputLayout: TextInputLayout?) {
            inputLayout?.isHelperTextEnabled = false
            inputLayout?.isErrorEnabled = false
            inputLayout?.endIconMode = TextInputLayout.END_ICON_NONE
        }

        fun validState(inputLayout: TextInputLayout?, icon: Int) {
            val myColorStateList = ColorStateList(
                arrayOf(intArrayOf(R.attr.state_activated), intArrayOf(R.attr.state_enabled)),
                intArrayOf(
                    Color.parseColor("#FF71BDC5"),  //1
                    Color.parseColor("#FF71BDC5")
                )
            )
            inputLayout?.isErrorEnabled = false
            inputLayout?.endIconMode = TextInputLayout.END_ICON_CUSTOM
            inputLayout?.setEndIconDrawable(icon)
            inputLayout?.setEndIconTintList(myColorStateList)
        }

        /**
         * Common method to catch on focus listener
         * When focus on the input text error will be removed
         * When focus out from the layout it will validate the content of the view
         */
        fun inputTextInitiateMethod(
            inputLayout: TextInputLayout?,
            inputEditText: TextInputEditText?,
            listener: InputTextListener
        ) {
            inputEditText?.onFocusChangeListener =
                View.OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                    if (hasFocus) {
                        normalState(inputLayout)
                    } else {
                        listener.validateUI()
                    }
                }
        }
    }
}