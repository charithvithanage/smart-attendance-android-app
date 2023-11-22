package lnbti.charithgtp01.smartattendanceadminapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class for User Object
 */
@Parcelize
data class User(
    val id: String,
    val nic: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    var userRole: String?,
    val dob: String,
    val userStatus: Boolean,
    val deviceID: String?,
    val employeeID:String,
    var lat: Double,
    var long: Double,
    var userType:String?
) : Parcelable{
    fun getUserStatusString(): String {
        val isActive = userStatus ?: false // Default to false if null
        return if (isActive) "Active" else "Inactive"
    }
}
