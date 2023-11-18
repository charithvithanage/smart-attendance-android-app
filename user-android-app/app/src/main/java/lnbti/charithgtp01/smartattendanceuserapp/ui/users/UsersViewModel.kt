package lnbti.charithgtp01.smartattendanceuserapp.ui.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.NO_INTERNET
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _usersList = MutableLiveData<List<User>>()
    val usersList get() = _usersList

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    private lateinit var allUsersList: List<User>

    /**
     * This will call when the View Model Created
     */
    init {
        getUsersList()
    }

    /**
     * Search View on text change listener
     * @param searchString Entering value
     */
    fun onSearchViewTextChanged(searchString: CharSequence?) {
        searchString?.toString()?.takeIf { it.isNotBlank() }?.let { value ->
            _usersList.value = filterApprovalList(value)
        } ?: run {
            _usersList.value = allUsersList
        }
    }

    /**
     * Get Server Response and Set values to live data
     */
    private fun getUsersList() {
        if (NetworkUtils.isNetworkAvailable()) {
            //Show Progress Dialog when click on the search view submit button
            _isDialogVisible.value = true
            /* View Model Scope - Coroutine */
            viewModelScope.launch {
                val resource = userRepository.getUsersFromDataSource()

                if (resource.data != null) {
                    allUsersList = resource.data.data
                    _usersList.value = allUsersList
                } else
                    _errorMessage.value = resource.error?.error

                /* Hide Progress Dialog with 1 Second delay after fetching the data list from the server */
                delay(1000L)
                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = NO_INTERNET
        }
    }

    /**
     * @param searchString Search View entered value
     * @return Data list filtered by user's full name
     */
    private fun filterApprovalList(searchString: String): List<User> {
        return allUsersList.run {
            filter { user ->
                val fullName = "${user.firstName} ${user.lastName}"
                fullName.lowercase().contains(searchString.lowercase())
            }
        }
    }
}