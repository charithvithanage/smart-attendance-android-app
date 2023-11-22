package lnbti.charithgtp01.smartattendanceuserapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject

/**
 * ViewModel for handling login-related operations.
 *
 * @property userRepository The repository for managing user data.
 * @property loginFormState LiveData for tracking the state of the login form.
 * @property loginResult LiveData for observing the login result.
 * @property isDialogVisible LiveData indicating the visibility of a dialog.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState = _loginForm

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult = _loginResult


    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    /**
     * Initiates the login process.
     *
     * @param deviceID The unique identifier for the device.
     * @param email The user's email address.
     * @param password The user's password.
     */
    fun login(deviceID: String, email: String, password: String) {

        // Handle the case when the device is connected to the network
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result =
                userRepository.login(
                    LoginRequest(
                        deviceID = deviceID,
                        username = email,
                        password = password
                    )
                )
            _loginResult.value = result
        }
    }

    /**
     * Validates and updates the login form state based on the provided username and password.
     *
     * @param username The user's entered username.
     * @param password The user's entered password.
     */
    fun loginDataChanged(username: String, password: String) {
        _loginForm.apply {
            value = when {
                !Validations.isUserNameValid(username) -> LoginFormState(nicError = R.string.invalid_username)
                !isPasswordValid(password) -> LoginFormState(passwordError = R.string.invalid_password)
                else -> LoginFormState(isDataValid = true)
            }
        }
    }
}