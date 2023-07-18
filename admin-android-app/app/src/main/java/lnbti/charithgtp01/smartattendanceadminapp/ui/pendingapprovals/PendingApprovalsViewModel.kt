package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapprovals

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
import javax.inject.Inject

@HiltViewModel
class PendingApprovalsViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _gitHubRepoList = MutableLiveData<List<User>>()
    val gitHubRepoList: LiveData<List<User>> get() = _gitHubRepoList

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    /**
     * This will call when the View Model Created
     */
    init {
        getPendingApprovalList()
    }

    /**
     * Search View Submit Button Click Event
     */
    fun onEditorAction(editeText: TextView?, actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

            val enteredValue = editeText?.text.toString()

            if (enteredValue.isNullOrBlank()) {
                //Empty value error Alert
                _errorMessage.value = userRepository.context.getString(R.string.no_internet)
            } else {
                filterApprovalList(editeText?.text.toString())
            }

            return true
        }
        return false
    }

    /**
     * Get Server Response and Set values to live data
     * @param inputText Pass entered value
     */
    private fun getPendingApprovalList() {

        val isNetworkAvailable = Utils.isOnline(userRepository.context.applicationContext)

        //If Network available call to backend API
        if (isNetworkAvailable) {
            //Show Progress Dialog when click on the search view submit button
            _isDialogVisible.value = true
            /* View Model Scope - Coroutine */
            viewModelScope.launch {
                val resource = userRepository.getPendingApprovalsFromDataSource()

                if (resource?.data != null) {
                    _gitHubRepoList.value = resource.data.data
                } else
                    _errorMessage.value = resource?.error?.error

                /* Hide Progress Dialog with 1 Second delay after fetching the data list from the server */
                delay(1000L)
                _isDialogVisible.value = false
            }
        } else {
            //Show Error Alert
            _errorMessage.value = userRepository.context.getString(R.string.no_internet)
        }

    }

    private fun filterApprovalList(searchString: String) {

    }
}