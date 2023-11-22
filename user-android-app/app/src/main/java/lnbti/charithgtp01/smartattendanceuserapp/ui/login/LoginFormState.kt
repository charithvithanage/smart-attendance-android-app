package lnbti.charithgtp01.smartattendanceuserapp.ui.login

/**
 * Represents the state of a login form.
 *
 * @property nicError An optional error code associated with the NIC (National Identification Card) input.
 *                     If `null`, there is no error; otherwise, it indicates an issue with the NIC input.
 * @property passwordError An optional error code associated with the password input.
 *                          If `null`, there is no error; otherwise, it indicates an issue with the password input.
 * @property isDataValid A boolean indicating whether the overall form data is considered valid.
 *                       It is `true` if both NIC and password inputs are valid; otherwise, it is `false`.
 *
 * @constructor Creates a [LoginFormState] with optional error codes for NIC and password and an overall
 *               validity status.
 *
 * @param nicError The error code associated with the NIC input, or `null` if there is no error.
 * @param passwordError The error code associated with the password input, or `null` if there is no error.
 * @param isDataValid The overall validity status of the form data. Default is `false`.
 */
data class LoginFormState(
    val nicError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)