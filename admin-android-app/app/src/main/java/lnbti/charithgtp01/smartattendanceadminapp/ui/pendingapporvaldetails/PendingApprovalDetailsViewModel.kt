package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapporvaldetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ApprovalRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.repositories.ApprovalRepository
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class PendingApprovalDetailsViewModel @Inject constructor(private val approvalRepository: ApprovalRepository) :
    ViewModel() {

    private val _pendingApprovalUser = MutableLiveData<User>()
    val pendingApprovalUser: LiveData<User> get() = _pendingApprovalUser

    //Server response live data
    private val _pendingApprovalResult = MutableLiveData<ApiCallResponse?>()
    val pendingApprovalResult: MutableLiveData<ApiCallResponse?> = _pendingApprovalResult

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

    fun submitApproval(isApprove: Boolean) {

        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result =
                approvalRepository.submitApproval(
                    ApprovalRequest(
                        "test",
                        "test"
                    )
                )
            _pendingApprovalResult.value = result
        }
    }
}