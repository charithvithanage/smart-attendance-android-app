package lnbti.charithgtp01.smartattendanceadminapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.MessageConstants
import lnbti.charithgtp01.smartattendanceadminapp.constants.MessageConstants.NO_INTERNET
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceadminapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceadminapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Validations
import lnbti.charithgtp01.smartattendanceadminapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: MutableLiveData<LoginResponse?> = _loginResult

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible
    fun login(email: String, password: String) {
        if (NetworkUtils.isNetworkAvailable()) {
            _isDialogVisible.value = true
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result = userRepository.login(LoginRequest(email, password))
                _loginResult.value = result
                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = NO_INTERNET
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