package lnbti.charithgtp01.smartattendanceadminapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceadminapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceadminapp.utils.Validations
import lnbti.charithgtp01.smartattendanceadminapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<ApiCallResponse?>()
    val loginResult: MutableLiveData<ApiCallResponse?> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result = userRepository.login(LoginRequest(email, password))
            _loginResult.value = result
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