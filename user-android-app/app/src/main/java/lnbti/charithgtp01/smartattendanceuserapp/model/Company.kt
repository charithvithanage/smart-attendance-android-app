package lnbti.charithgtp01.smartattendanceuserapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(
    val companyID: String,
    val companyName: String,
    val status: Boolean
) : Parcelable
