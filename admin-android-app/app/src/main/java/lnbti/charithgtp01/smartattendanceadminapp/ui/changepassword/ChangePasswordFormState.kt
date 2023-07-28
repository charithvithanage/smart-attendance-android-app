package lnbti.charithgtp01.smartattendanceadminapp.ui.changepassword

/**
 * Data validation state of the login form.
 */
data class ChangePasswordFormState(
    val currentPasswordError: Int? = null,
    val newPasswordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val isDataValid: Boolean = false
)