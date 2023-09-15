package lnbti.charithgtp01.smartattendanceadminapp.bindingadapters

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.databinding.BindingAdapter
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import java.util.Locale

/**
 * Bind Adapter to set values to profile icon Text View
 */
object BindingAdapters {
    @BindingAdapter("circularBg")
    @JvmStatic
    fun setCircularBg(view: TextView, profile: User?) {
        val initialLetter: String? = profile?.firstName?.take(1)?.uppercase()
        view.text = initialLetter
        //Set text color according to the first letter
        view.setTextColor(Color.parseColor(getTextColor(initialLetter)))
        val cornerRadius = view.context.resources.getDimension(R.dimen.rounded_corner_radius)
        val color = Color.parseColor(getBgColor(initialLetter))
        val roundedDrawable = createRoundedDrawable(cornerRadius, color)
        view.background = roundedDrawable
    }

    /**
     * Create colored, rounded drawable
     */
    private fun createRoundedDrawable(cornerRadius: Float, color: Int): Drawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = cornerRadius
        drawable.setColor(color)
        return drawable
    }

    /**
     * Return Hex Color code of text color according to the first letter of the name
     */
    private fun getTextColor(firstLetter: String?): String {
        var colorCode: String
        when (firstLetter?.uppercase(Locale.getDefault())) {
            "A" -> colorCode = "#FFFF0000"
            "B" -> colorCode = "#FF9000FF"
            "C" -> colorCode = "#FF0025FF"
            "D" -> colorCode = "#FF00BBFF"
            "E" -> colorCode = "#FF00FFA5"
            "F" -> colorCode = "#FF5DFF00"
            "G" -> colorCode = "#FFFFE200"
            "H" -> colorCode = "#FF000000"
            "I" -> colorCode = "#FFA402A8"
            "J" -> colorCode = "#FF0258A8"
            "K" -> colorCode = "#FF02A858"
            "L" -> colorCode = "#FF90A802"
            "M" -> colorCode = "#FF02A88C"
            "N" -> colorCode = "#FF6CA802"
            "O" -> colorCode = "#FF7EB4D4"
            "P" -> colorCode = "#FFB9E678"
            "Q" -> colorCode = "#FF78E6E1"
            "R" -> colorCode = "#FFFF006D"
            "S" -> colorCode = "#FF9EBC6D"
            "T" -> colorCode = "#FFBC6DBD"
            "U" -> colorCode = "#FFBC9F6D"
            "V" -> colorCode = "#FF6DBCAD"
            "W" -> colorCode = "#FFBC6DA4"
            "X" -> colorCode = "#FFBF5900"
            "Y" -> colorCode = "#FFFC5757"
            "Z" -> colorCode = "#FFB8B6A5"
            else -> colorCode = "#FFFF0000"
        }
        return colorCode
    }

    /**
     * Return Hex Color code bg color according to the first letter of the name
     */
    private fun getBgColor(firstLetter: String?): String {
        var colorCode: String
        when (firstLetter?.uppercase(Locale.getDefault())) {
            "A" -> colorCode = "#32FF0000"
            "B" -> colorCode = "#329000FF"
            "C" -> colorCode = "#320025FF"
            "D" -> colorCode = "#3200BBFF"
            "E" -> colorCode = "#3200FFA5"
            "F" -> colorCode = "#325DFF00"
            "G" -> colorCode = "#32FFE200"
            "H" -> colorCode = "#32000000"
            "I" -> colorCode = "#32A402A8"
            "J" -> colorCode = "#320258A8"
            "K" -> colorCode = "#3202A858"
            "L" -> colorCode = "#3290A802"
            "M" -> colorCode = "#3202A88C"
            "N" -> colorCode = "#326CA802"
            "O" -> colorCode = "#327EB4D4"
            "P" -> colorCode = "#32B9E678"
            "Q" -> colorCode = "#3278E6E1"
            "R" -> colorCode = "#32FF006D"
            "S" -> colorCode = "#329EBC6D"
            "T" -> colorCode = "#32BC6DBD"
            "U" -> colorCode = "#32BC9F6D"
            "V" -> colorCode = "#326DBCAD"
            "W" -> colorCode = "#32BC6DA4"
            "X" -> colorCode = "#32BF5900"
            "Y" -> colorCode = "#32FC5757"
            "Z" -> colorCode = "#32B8B6A5"
            else -> colorCode = "#32FF0000"
        }
        return colorCode
    }

    //User status binding adapter
    @BindingAdapter("booleanToString")
    fun setBooleanToString(textView: TextView, isTrue: Boolean) {
        textView.text = if (isTrue) "True String" else "False String"
    }
}