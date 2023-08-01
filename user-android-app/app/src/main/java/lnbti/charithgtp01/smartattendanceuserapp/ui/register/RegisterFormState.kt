package lnbti.charithgtp01.smartattendanceuserapp.ui.register

/**
 * Data validation state of the login form.
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