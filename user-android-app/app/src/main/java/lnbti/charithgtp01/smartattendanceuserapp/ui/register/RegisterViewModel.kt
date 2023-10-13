package lnbti.charithgtp01.smartattendanceuserapp.ui.register

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.constants.MessageConstants
import lnbti.charithgtp01.smartattendanceuserapp.constants.MessageConstants.NO_INTERNET
import lnbti.charithgtp01.smartattendanceuserapp.constants.MessageConstants.USER_REGISTERED_SUCCESS
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.Company
import lnbti.charithgtp01.smartattendanceuserapp.model.RegisterRequest
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.ui.login.LoginActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isEmailValid
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isMobileNumberValid
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isNICValid
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject

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
    val registerForm: LiveData<RegisterFormState> = _registerForm

    //Server response live data
    private val _registerResult = MutableLiveData<ApiCallResponse?>()
    val registerResult: MutableLiveData<ApiCallResponse?> = _registerResult

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    //Error Message Live Data
    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess: LiveData<Boolean?> get() = _isSuccess

    // Function to set the selected RadioButton value
    fun setSelectedRadioButtonValue(value: String) {
        gender = value
    }

    fun register() {
        if (NetworkUtils.isNetworkAvailable()) {
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

                if (result?.success == true) {
                    _isSuccess.value = true
                } else if (result?.data != null) {
                    _errorMessage.value = result.data.toString()
                }
                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = NO_INTERNET
        }
    }

    /**
     * Validate Form Data
     * If all fields are valid call to api
     * Else show an error in the edit text
     */

    fun validateFields() {

        if (firstName.isNullOrBlank()) {
            _registerForm.value =
                RegisterFormState(firstNameError = R.string.enter_first_name)
        } else if (lastName.isNullOrBlank()) {
            _registerForm.value =
                RegisterFormState(lastNameError = R.string.enter_last_name)
        } else if (!isNICValid(nic)) {
            _registerForm.value =
                RegisterFormState(nicError = R.string.enter_nic)
        } else if (!isEmailValid(email)) {
            _registerForm.value =
                RegisterFormState(emailError = R.string.enter_email)
        } else if (!isMobileNumberValid(contact)) {
            _registerForm.value =
                RegisterFormState(contactError = R.string.enter_contact)
        } else if (dob.isNullOrBlank()) {
            _registerForm.value =
                RegisterFormState(dobError = R.string.enter_dob)
        } else if (gender == null) {
            _registerForm.value =
                RegisterFormState(genderError = R.string.select_gender)
        } else if (userName.isNullOrBlank()) {
            _registerForm.value =
                RegisterFormState(userNameError = R.string.enter_user_name)
        } else if (!isPasswordValid(newPassword)) {
            _registerForm.value =
                RegisterFormState(newPasswordError = R.string.invalid_password)
        } else if (newPassword != confirmPassword) {
            _registerForm.value =
                RegisterFormState(confirmPasswordError = R.string.invalid_confirm_password)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
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