package lnbti.charithgtp01.smartattendanceadminapp.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val nicError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)