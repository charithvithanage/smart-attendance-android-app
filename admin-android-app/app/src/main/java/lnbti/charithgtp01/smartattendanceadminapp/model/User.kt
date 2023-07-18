package lnbti.charithgtp01.smartattendanceadminapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class for User Object
 */
@Parcelize
data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    private val nullable_gender: String?,
    private val nullable_user_role: String?,
    private val nullable_dob: String?,
    val avatar: String
) : Parcelable {
    val gender: String
        get() = nullable_gender ?: "Male"

    val user_role: String
        get() = nullable_gender ?: "Technician"

    val dob: String
        get() = nullable_gender ?: "1991/05/03"
}
