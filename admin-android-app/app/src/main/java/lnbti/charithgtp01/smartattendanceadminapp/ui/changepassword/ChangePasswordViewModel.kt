package lnbti.charithgtp01.smartattendanceadminapp.ui.changepassword

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.MessageConstants
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceadminapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceadminapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject


@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //variable that will listen to user's input
    var currentPassword: String? = null

    var newPassword: String? = null

    var confirmPassword: String? = null

    //Form live data
    private val _changePasswordForm = MutableLiveData<ChangePasswordFormState>()
    val changePasswordForm: LiveData<ChangePasswordFormState> = _changePasswordForm

    //Server response live data
    private val _changePasswordResult = MutableLiveData<ApiCallResponse?>()
    val changePasswordResult: MutableLiveData<ApiCallResponse?> = _changePasswordResult

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun changePassword(id: String) {
        if (NetworkUtils.isNetworkAvailable()) {
            _isDialogVisible.value = true
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result =
                    userRepository.changePassword(
                        ChangePasswordRequest(
                            id,
                            currentPassword,
                            newPassword
                        )
                    )
                changePasswordResult.value = result
                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = MessageConstants.NO_INTERNET
        }
    }

    /**
     * Validate Form Data
     * If all fields are valid call to api
     * Else show an error in the edit text
     */

    fun validateFields() {
        if (!isPasswordValid(currentPassword)) {
            _changePasswordForm.value =
                ChangePasswordFormState(currentPasswordError = R.string.invalid_password)
        } else if (!isPasswordValid(newPassword)) {
            _changePasswordForm.value =
                ChangePasswordFormState(newPasswordError = R.string.invalid_password)
        } else if (newPassword != confirmPassword) {
            _changePasswordForm.value =
                ChangePasswordFormState(confirmPasswordError = R.string.invalid_confirm_password)
        } else {
            _changePasswordForm.value = ChangePasswordFormState(isDataValid = true)
        }
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