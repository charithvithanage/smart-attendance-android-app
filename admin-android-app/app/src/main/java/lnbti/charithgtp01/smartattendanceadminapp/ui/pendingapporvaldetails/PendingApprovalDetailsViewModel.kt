package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapporvaldetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.constants.MessageConstants
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ApprovalRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.repositories.ApprovalRepository
import lnbti.charithgtp01.smartattendanceadminapp.utils.NetworkUtils
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class PendingApprovalDetailsViewModel @Inject constructor(private val approvalRepository: ApprovalRepository) :
    ViewModel() {

    var deviceID: String? = null

    // Define LiveData for the selected item
    private val _selectedItem = MutableLiveData<String?>()
    val selectedItem: MutableLiveData<String?> = _selectedItem

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
        deviceID = pendingApprovalUser.deviceID
        _pendingApprovalUser.value = pendingApprovalUser
    }

    // Function to set the selected item
    fun updateSelectedItem(item: String?) {
        _selectedItem.value = item
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
                            userRole = userRole,
                            userStatus = isApprove
                        )
                    )
                _pendingApprovalResult.value = result
                _isDialogVisible.value = false

            }
        } else {
            _errorMessage.value = MessageConstants.NO_INTERNET
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
            _errorMessage.value = MessageConstants.NO_INTERNET
        }
    }
}