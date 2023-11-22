package lnbti.charithgtp01.smartattendanceuserapp.ui.changepassword

/**
 * Represents the state of a change password form.
 *
 * @property currentPasswordError An optional error resource ID for the current password field.
 *                               If null, there is no error.
 * @property newPasswordError An optional error resource ID for the new password field.
 *                            If null, there is no error.
 * @property confirmPasswordError An optional error resource ID for the confirm password field.
 *                                If null, there is no error.
 * @property isDataValid A boolean indicating whether the form data is valid or not.
 */
data class ChangePasswordFormState(
    val currentPasswordError: Int? = null,
    val newPasswordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val isDataValid: Boolean = false
)