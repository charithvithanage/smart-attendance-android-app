package lnbti.charithgtp01.smartattendanceuserapp.ui.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class UserDetailsViewModel @Inject constructor() : ViewModel() {

    private val _pendingApprovalUser = MutableLiveData<User>()
    val pendingApprovalUser get() = _pendingApprovalUser

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    /**
     * Set Pending Approval User Object to Live Data
     * @param Selected Pending Approval User Object
     */
    fun setPendingApprovalUserData(pendingApprovalUser: User) {
        _pendingApprovalUser.value = pendingApprovalUser
    }


}