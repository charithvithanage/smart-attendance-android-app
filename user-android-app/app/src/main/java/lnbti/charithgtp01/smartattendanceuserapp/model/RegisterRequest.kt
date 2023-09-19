package lnbti.charithgtp01.smartattendanceuserapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequest(
    val employeeID: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val contact: String?,
    val nic: String?,
    val gender: String?,
    val dob: String?,
    val username: String?,
    val password: String?,
    val userStatus: Boolean
) : Parcelable
