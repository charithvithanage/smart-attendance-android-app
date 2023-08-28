package lnbti.charithgtp01.smartattendanceuserapp.model

import android.location.Location
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class for User Object
 */
@Parcelize
data class User(
    val nic: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val userRole: String?,
    val dob: String?,
    val userStatus: String?,
    val avatar: String,
    var lat: Double,
    var long: Double
) : Parcelable
