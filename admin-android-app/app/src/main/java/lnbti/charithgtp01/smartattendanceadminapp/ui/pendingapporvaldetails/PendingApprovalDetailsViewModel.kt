package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapporvaldetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ApprovalRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.repositories.ApprovalRepository
import lnbti.charithgtp01.smartattendanceadminapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.findPositionInList
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class PendingApprovalDetailsViewModel @Inject constructor(private val approvalRepository: ApprovalRepository) :
    ViewModel() {

    var deviceID: String? = null

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

    val selectedUserTypePosition = MutableLiveData<Int>()
    val selectedUserRolePosition = MutableLiveData<Int>()

    val userTypeSpinnerItems = Utils.userTypes
    private val selectedUserType = MutableLiveData<String>()

    val userRoleSpinnerItems = Utils.userRoles
    private val selectedUserRole = MutableLiveData<String>()

    /**
     * Set Pending Approval User Object to Live Data
     * @param Selected Pending Approval User Object
     */
    fun setPendingApprovalUserData(pendingApprovalUser: User) {
        deviceID = pendingApprovalUser.deviceID
        _pendingApprovalUser.value = pendingApprovalUser
        selectedUserRole.value = pendingApprovalUser.userRole
        selectedUserType.value = pendingApprovalUser.userType
        selectedUserTypePosition.value = findPositionInList(userTypeSpinnerItems, pendingApprovalUser.userType)
        selectedUserRolePosition.value = findPositionInList(userRoleSpinnerItems, pendingApprovalUser.userRole)

    }

    // Function to set the selected user role
    fun selectUserRole(item: String) {
        selectedUserRole.value = item
    }

    // Function to set the selected user type
    fun selectUserType(item: String) {
        selectedUserType.value = item
    }

    fun submitApproval(nic: String, deviceID: String?, userRole: String?, isApprove: Boolean) {
        if (NetworkUtils.isNetworkAvailable()) {
            _isDialogVisible.value = true
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result =
                    approvalRepository.submitApproval(
                        ApprovalRequest(
                            nic = nic,
                            deviceID = deviceID,
                            userRole = selectedUserRole.value,
                            userStatus = isApprove,
                            userType = selectedUserType.value
                        )
                    )
                _pendingApprovalResult.value = result
                _isDialogVisible.value = false

            }
        } else {
            _errorMessage.value = ResourceConstants.NO_INTERNET
        }
    }

    fun rejectApproval(nic: String) {
        if (NetworkUtils.isNetworkAvailable()) {
            _isDialogVisible.value = true
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result =
                    approvalRepository.rejectApproval(
                        nic
                    )
                _pendingApprovalResult.value = result
                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = ResourceConstants.NO_INTERNET
        }
    }
}