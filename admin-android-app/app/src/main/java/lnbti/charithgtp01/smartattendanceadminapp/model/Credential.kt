package lnbti.charithgtp01.smartattendanceadminapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Credential(
    val username: String,
    val password: String
) : Parcelable