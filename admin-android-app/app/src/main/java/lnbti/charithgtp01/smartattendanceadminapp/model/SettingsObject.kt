package lnbti.charithgtp01.smartattendanceadminapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class for User Object
 */
@Parcelize
data class SettingsObject(
    val icon: Int,
    val name: String
) : Parcelable
