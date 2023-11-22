package lnbti.charithgtp01.smartattendanceuserapp.model

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
    val userRole: String,
    val dob: String,
    val userStatus: Boolean,
    val employeeID:String,
    val userType:String,
    var lat: Double,
    var long: Double
) : Parcelable{
    fun getUserStatusString(): String {
        val isActive = userStatus // Default to false if null
        return if (isActive) "Active" else "Inactive"
    }
}
