package lnbti.charithgtp01.smartattendanceuserapp.ui.login

import android.content.Context
import android.graphics.Bitmap.Config
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.ui.login.LoginFormState
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject

/**
 * Login Page View Model
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: MutableLiveData<LoginResponse?> = _loginResult


    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    fun login(email: String, password: String) {
        if (NetworkUtils.isNetworkAvailable()) {
            // Handle the case when the device is connected to the network
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result = userRepository.login(LoginRequest(email, password))
                _loginResult.value = result
            }
        } else {
            // Handle the case when the device is not connected to the network
            _loginResult.value = LoginResponse(false, "No Internet")
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!Validations.isUserNameValid(username)) {
            _loginForm.value = LoginFormState(nicError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

}