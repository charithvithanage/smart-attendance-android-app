package lnbti.charithgtp01.smartattendanceuserapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDateToDayOfWeek
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDateWithMonth

@Parcelize
data class AttendanceData(
    val date: String,
    val inTime: String,
    val outTime: String,
    val userID: String
) : Parcelable {

    fun getFormattedDate(): String {
        return formatDateWithMonth(date)
    }
    fun getFormattedDayOfTheWeek(): String {
        return formatDateToDayOfWeek(date)
    }

}
