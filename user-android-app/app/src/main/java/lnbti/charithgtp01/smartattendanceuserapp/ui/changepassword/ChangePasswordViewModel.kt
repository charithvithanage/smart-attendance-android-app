package lnbti.charithgtp01.smartattendanceuserapp.ui.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject

/**
 * ViewModel for handling password change functionality.
 *
 * This ViewModel is responsible for managing the state related to changing a user's password.
 * It interacts with the [userRepository] to perform the actual password change through the API.
 *
 * @property userRepository The repository responsible for user data and API interactions.
 * @property currentPassword The current password entered by the user.
 * @property newPassword The new password entered by the user.
 * @property confirmPassword The confirmation of the new password entered by the user.
 * @property changePasswordForm LiveData representing the state of the change password form.
 * @property changePasswordResult LiveData representing the result of the password change API call.
 * @property isDialogVisible LiveData representing the visibility of a progress dialog.
 */
@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    //variable that will listen to user's input
    var currentPassword: String? = null

    var newPassword: String? = null

    var confirmPassword: String? = null

    //Form live data
    private val _changePasswordForm = MutableLiveData<ChangePasswordFormState>()
    val changePasswordForm = _changePasswordForm

    //Server response live data
    private val _changePasswordResult = MutableLiveData<ApiCallResponse?>()
    val changePasswordResult = _changePasswordResult

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    /**
     * Initiates the process of changing the user's password.
     *
     * This function is responsible for triggering the password change process by calling
     * [UserRepository.changePassword]. It updates the [changePasswordResult] LiveData with
     * the API response and manages the visibility of the progress dialog.
     *
     * @param nic The user's NIC (National Identification Card) for identification.
     */
    fun changePassword(nic: String) {
        _isDialogVisible.value = true

        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result =
                userRepository.changePassword(
                    ChangePasswordRequest(
                        nic,
                        oldPassword = currentPassword,
                        newPassword = newPassword
                    )
                )
            changePasswordResult.value = result

            _isDialogVisible.value = false
        }
    }

    /**
     * Validates the input fields of the change password form.
     *
     * This function checks the validity of the current password, new password, and
     * confirmation password. If any of the fields is invalid, it updates the
     * [changePasswordForm] LiveData with the appropriate error message.
     */
    fun validateFields() {
        _changePasswordForm.value = run {
            when {
                !isPasswordValid(currentPassword) -> ChangePasswordFormState(currentPasswordError = R.string.invalid_password)
                !isPasswordValid(newPassword) -> ChangePasswordFormState(newPasswordError = R.string.invalid_password)
                newPassword != confirmPassword -> ChangePasswordFormState(confirmPasswordError = R.string.invalid_confirm_password)
                else -> ChangePasswordFormState(isDataValid = true)
            }
        }
    }

}