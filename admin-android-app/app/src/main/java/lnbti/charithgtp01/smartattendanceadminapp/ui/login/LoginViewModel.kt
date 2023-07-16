package lnbti.charithgtp01.smartattendanceadminapp.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.data.LoginRepository
import lnbti.charithgtp01.smartattendanceadminapp.data.Result

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(nicError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(userNIC: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        var valid: Boolean = if (userNIC.isNullOrBlank()) {
            false
        } else {
            if (userNIC.length == 12) {
                userNIC.matches(regex)
            } else if (userNIC.length == 10) {
                val first9Characters: String = userNIC.substring(0, 9)
                val lastCharacter: String = userNIC.substring(userNIC.length - 1)
                if (first9Characters.matches(regex)) {
                    if (lastCharacter.equals("v", ignoreCase = true)) {
                        true
                    } else lastCharacter.equals("x", ignoreCase = true)
                } else {
                    false
                }
            } else {
                false
            }
        }
        return valid

    }


    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}