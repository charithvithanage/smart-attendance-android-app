package lnbti.charithgtp01.smartattendanceuserapp.ui.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.NO_INTERNET
import lnbti.charithgtp01.smartattendanceuserapp.model.Resource
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import java.util.Locale
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _apiResult = MutableLiveData<Resource?>()
    val apiResult = _apiResult

    private val _usersList = MutableLiveData<List<User>>()
    val usersList get() = _usersList

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
        /* View Model Scope - Coroutine */
        viewModelScope.launch {
            _apiResult.value = userRepository.getUsersFromDataSource()
        }

    }

    /**
     * Filters the list of users based on the provided search string.
     *
     * @param searchString The search string entered in the search view.
     * @return The filtered list of users.
     */
    private fun filterApprovalList(searchString: String): List<User> {
        return allUsersList.filter { user ->
            with(user) {
                ("$firstName $lastName").lowercase(Locale.getDefault()).contains(
                    searchString.lowercase(
                        Locale.getDefault()
                    )
                )
            }
        }
    }

    fun setUsers(list: List<User>) {
        allUsersList = list
        _usersList.value = list
    }
}