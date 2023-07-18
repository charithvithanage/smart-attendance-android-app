package lnbti.charithgtp01.smartattendanceadminapp.ui.userdetails

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.isOnline
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class PendingApprovalDetailsViewModel @Inject constructor() : ViewModel() {

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
}