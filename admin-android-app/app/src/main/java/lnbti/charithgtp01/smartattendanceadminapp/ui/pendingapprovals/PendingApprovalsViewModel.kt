package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapprovals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceadminapp.utils.NetworkUtils
import javax.inject.Inject

/**
 * Pending Approval View Model
 */
@HiltViewModel
class PendingApprovalsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _pendingApprovalList = MutableLiveData<List<User>>()
    val pendingApprovalList: LiveData<List<User>> get() = _pendingApprovalList

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    lateinit var allUsersList: List<User>

    /**
     * This will call when the View Model Created
     */
    init {
        getPendingApprovalList()
    }

    /**
     * Search View on text change listener
     * @param searchString Entering value
     */
    fun onSearchViewTextChanged(searchString: CharSequence?) {
        val value = searchString.toString()
        if (value.isNullOrBlank()) {
            _pendingApprovalList.value = allUsersList
        } else {
            _pendingApprovalList.value = filterApprovalList(value)
        }
    }

    /**
     * Get Server Response and Set values to live data
     * @param inputText Pass entered value
     */
    private fun getPendingApprovalList() {
        //If Network available call to backend API
        if (NetworkUtils.isNetworkAvailable()) {
            //Show Progress Dialog when click on the search view submit button
            _isDialogVisible.value = true
            /* View Model Scope - Coroutine */
            viewModelScope.launch {
                val resource = userRepository.getPendingApprovalsFromDataSource()

                if (resource?.data != null) {
                    allUsersList = resource.data.data
                    _pendingApprovalList.value = allUsersList
                } else
                    _errorMessage.value = resource?.error?.error

                _isDialogVisible.value = false
            }
        } else {
            //Show Error Alert
            _errorMessage.value = ResourceConstants.NO_INTERNET
        }

    }

    /**
     * @param searchString Search View entered value
     * @return Data list filtered by user's full name
     */
    private fun filterApprovalList(searchString: String): List<User>? {
        // to get the result as list
        return allUsersList?.filter { s ->
            (s.firstName + " " + s.lastName).contains(
                searchString
            )
        }
    }
}