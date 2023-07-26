package lnbti.charithgtp01.smartattendanceadminapp.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActionBarLayoutBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.InputTextListener
import java.util.Locale

/**
 * A Utils class containing UI related Methods
 */
class UIUtils {

    companion object {

        /**
         * Action Bar Initiation
         * @param parent Parent layout of the action bar layout
         * @param activityTitle Action Bar Title String
         * @param actionBarListener Action Bar image button listener
         */
        fun initiateActionBar(
            parent: ViewGroup,
            activityTitle: String,
            actionBarListener: ActionBarListener
        ) {
            // Inflate the layout using DataBindingUtil
            val binding =
                ActionBarLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, true)
            binding.tvTitle.text = activityTitle.uppercase(Locale.getDefault())
            binding.backBtn.setOnClickListener {
                actionBarListener.backPressed()
            }
            binding.btnHome?.setOnClickListener {
                actionBarListener.homePressed()
            }
        }

        /**
         * Convert dp values to px
         */
        private fun convertDpToPixel(dp: Float, context: Context?): Float {
            if (context != null)
                return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
            return 0.0f
        }

        /**
         * Change Element with according to the screen size
         * @param context Context of the View
         * @param view layout element
         * @param widthRatio View width according to widthRatio*(display/width)
         * @param width Screen divide in to (display/width)
         * Ex: width=4 and widthRatio=3 means Actual element width= (Screen Width/4)*3
         * @param margin Horizontal Margin
         */
        fun changeUiSize(context: Context?, view: View, widthRatio: Int, width: Int, margin: Int) {
            if (context != null) {
                val display = context.resources.displayMetrics
                val params = view.layoutParams
                params.width = (display.widthPixels * widthRatio / width - convertDpToPixel(
                    margin.toFloat(),
                    context
                ) * 2).toInt()
                view.layoutParams = params
            }
        }

        /**
         * Change Element with according to the screen size
         * Element has no margin
         * @param context Context of the View
         * @param view layout element
         * @param widthRatio View width according to widthRatio*(display/width)
         * @param width Screen divide in to (display/width)
         * Ex: width=4 and widthRatio=3 means Actual element width= (Screen Width/4)*3
         */
        fun changeUiSize(context: Context, view: View, widthRatio: Int, width: Int) {
            val display = context.resources.displayMetrics
            val params = view.layoutParams
            params.width = display.widthPixels * widthRatio / width
            view.layoutParams = params
        }

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