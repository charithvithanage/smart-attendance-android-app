package lnbti.charithgtp01.smartattendanceuserapp.ui.register

/**
 * Represents the state of a registration form, including validation error messages and overall data validity.
 *
 * @property firstNameError The error code associated with the first name input. Null if no error.
 * @property lastNameError The error code associated with the last name input. Null if no error.
 * @property nicError The error code associated with the NIC (National Identification Card) input. Null if no error.
 * @property employeeIDError The error code associated with the employee ID input. Null if no error.
 * @property dobError The error code associated with the date of birth input. Null if no error.
 * @property genderError The error code associated with the gender input. Null if no error.
 * @property contactError The error code associated with the contact number input. Null if no error.
 * @property emailError The error code associated with the email input. Null if no error.
 * @property userNameError The error code associated with the username input. Null if no error.
 * @property newPasswordError The error code associated with the new password input. Null if no error.
 * @property confirmPasswordError The error code associated with the confirm password input. Null if no error.
 * @property isDataValid A boolean flag indicating whether the overall data in the form is valid or not.
 */
data class RegisterFormState(
    var firstNameError: Int? = null,
    var lastNameError: Int? = null,
    var nicError: Int? = null,
    var employeeIDError: Int? = null,
    var dobError: Int? = null,
    var genderError: Int? = null,
    var contactError: Int? = null,
    var emailError: Int? = null,
    var userNameError: Int? = null,
    var newPasswordError: Int? = null,
    var confirmPasswordError: Int? = null,
    val isDataValid: Boolean = false
)