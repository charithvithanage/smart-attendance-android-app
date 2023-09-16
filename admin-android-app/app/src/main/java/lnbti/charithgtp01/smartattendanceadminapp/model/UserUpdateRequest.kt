package lnbti.charithgtp01.smartattendanceadminapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class for User Object
 */
@Parcelize
data class UserUpdateRequest(
    var nic: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var gender: String,
    var userRole: String?,
    var dob: String?,
    var userStatus: Boolean?,
    var deviceID: String?
) : Parcelable{

}
