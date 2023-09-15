package lnbti.charithgtp01.smartattendanceadminapp.ui.userdetails

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
class UserDetailsViewModel @Inject constructor() : ViewModel() {

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
    fun setPendingApprovalUserData(pendingApprovalUser: User) {
        _pendingApprovalUser.value = pendingApprovalUser
    }

    private val _isUserActive = MutableLiveData<Boolean>()

    // Function to compute the status string based on the boolean condition
    fun getUserStatusString(): String {
        val isActive = _isUserActive.value ?: false // Default to false if null
        return if (isActive) "Active" else "Inactive"
    }

}