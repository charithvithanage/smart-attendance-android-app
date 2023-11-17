package lnbti.charithgtp01.smartattendanceuserapp.ui.register

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.Company
import lnbti.charithgtp01.smartattendanceuserapp.model.RegisterRequest
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isEmailValid
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isMobileNumberValid
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isNICValid
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject
/**
 * [ViewModel] class for handling registration-related logic and data manipulation.
 *
 * @property userRepository The repository responsible for handling user data operations.
 * @property firstName First name of the user.
 * @property lastName Last name of the user.
 * @property nic National Identity Card number of the user.
 * @property employeeID Employee ID of the user.
 * @property dob Date of birth of the user.
 * @property gender Gender of the user.
 * @property contact Contact number of the user.
 * @property email Email address of the user.
 * @property userName User's chosen username.
 * @property newPassword User's new password for registration.
 * @property confirmPassword Confirmation of the new password.
 * @property _registerForm LiveData holding the registration form state.
 * @property registerForm Public accessor for [_registerForm].
 * @property _isDialogVisible LiveData indicating the visibility of a progress dialog.
 * @property isDialogVisible Public accessor for [_isDialogVisible].
 * @property _errorMessage LiveData holding error messages during registration.
 * @property errorMessage Public accessor for [_errorMessage].
 * @property _isSuccess LiveData indicating the success of the registration process.
 * @property isSuccess Public accessor for [_isSuccess].
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) :
    ViewModel() {

    var firstName: String? = null
    var lastName: String? = null
    var nic: String? = null
    var employeeID: String? = null
    var dob: String? = null
    var gender: String? = null
    var contact: String? = null
    var email: String? = null
    var userName: String? = null
    var newPassword: String? = null
    var confirmPassword: String? = null

    //Form live data
    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerForm = _registerForm

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    //Error Message Live Data
    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess get() = _isSuccess

    /**
     * Sets the selected RadioButton value for the user's gender.
     *
     * @param value The selected gender value.
     */
    fun setSelectedRadioButtonValue(value: String) {
        gender = value
    }

    /**
     * Initiates the registration process, making an asynchronous call to the repository.
     * Updates [_isDialogVisible], [_isSuccess], and [_errorMessage] LiveData accordingly.
     */
    fun register() {
        _isDialogVisible.value = true
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result =
                userRepository.register(
                    RegisterRequest(
                        employeeID,
                        firstName,
                        lastName,
                        email,
                        contact,
                        nic,
                        gender,
                        dob,
                        userName,
                        newPassword,
                        false
                    )
                )

            result?.run {
                when {
                    success == true -> _isSuccess.value = true
                    data != null -> _errorMessage.value = data.toString()
                }
            }
            _isDialogVisible.value = false
        }
    }

    /**
     * Sets a custom error message for the registration process.
     *
     * @param errorMessage The error message to be displayed.
     */
    fun setErrorMessage(errorMessage: String) {
        _errorMessage.value = errorMessage
    }

    /**
     * Validate Form Data
     * If all fields are valid call to api
     * Else show an error in the edit text
     */

    fun validateFields() {
        _registerForm.value.apply {
            when {
                firstName.isNullOrBlank() -> RegisterFormState(firstNameError = R.string.enter_first_name)
                lastName.isNullOrBlank() -> RegisterFormState(lastNameError = R.string.enter_last_name)
                !isNICValid(nic) -> RegisterFormState(nicError = R.string.enter_nic)
                !isEmailValid(email) -> RegisterFormState(emailError = R.string.enter_email)
                !isMobileNumberValid(contact) -> RegisterFormState(contactError = R.string.enter_contact)
                dob.isNullOrBlank() -> RegisterFormState(dobError = R.string.enter_dob)
                gender == null -> RegisterFormState(genderError = R.string.select_gender)
                userName.isNullOrBlank() -> RegisterFormState(userNameError = R.string.enter_user_name)
                !isPasswordValid(newPassword) -> RegisterFormState(newPasswordError = R.string.invalid_password)
                newPassword != confirmPassword -> RegisterFormState(confirmPasswordError = R.string.invalid_confirm_password)
                else -> RegisterFormState(isDataValid = true)
            }
        }
    }

    fun setCompany(company: Company) {
        employeeID = company.companyID
    }

    /**
     * Common function to set the OnFocusChangeListener
     * And attach it to the TextInputEditText fields:
     */
    fun setFocusChangeListener(editText: TextInputEditText) {
        editText.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            run {
                validateFields()
            }
        }
    }


}