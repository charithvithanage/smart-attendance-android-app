package lnbti.charithgtp01.smartattendanceuserapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttendanceData(
    val date: String,
    val inTime: String,
    val outTime: String,
    val userID: String
) : Parcelable
