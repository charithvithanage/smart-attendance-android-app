package lnbti.charithgtp01.smartattendanceadminapp.ui.useredit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class UserEditViewModel @Inject constructor() : ViewModel() {

    private val _pendingApprovalUser = MutableLiveData<User>()
    val pendingApprovalUser: LiveData<User> get() = _pendingApprovalUser

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    /**
     * Set Pending Approval User Object to Live Data
     * @param Selected Pending Approval User Object
     */
    fun setPendingApprovalUserData(selectedUser: User) {
        _pendingApprovalUser.value = selectedUser
    }
}