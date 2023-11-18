package lnbti.charithgtp01.smartattendanceuserapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.NO_INTERNET
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceData
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.AttendanceRepository
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDate
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the home screen, responsible for managing user data and attendance information.
 *
 * @property userRepository Repository for accessing user data.
 * @property attendanceRepository Repository for accessing attendance data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _usersList = MutableLiveData<List<User>>()
    val usersList get() = _usersList

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    private var allUsersList: List<User>? = null

    private var today: Date = Date()

    private val _dateString = MutableLiveData<String>()
    val dateString get() = _dateString

    //Live data for Get Attendance Response
    private val _attendanceResult = MutableLiveData<ApiCallResponse?>()
    val attendanceResult = _attendanceResult

    var name: String? = null
    private val _attendanceData = MutableLiveData<AttendanceData?>()
    val attendanceData = _attendanceData

    /**
     * Initializes the ViewModel and sets the current date as the default value for [_dateString].
     */
    init {
        _dateString.value = formatDate(today)
    }

    /**
     * Listener for changes in the search view's text. Updates the [_usersList] accordingly.
     *
     * @param searchString The entered search string.
     */
    fun onSearchViewTextChanged(searchString: CharSequence?) {
        val value = searchString.toString()
        _usersList.value = value.takeIf { it.isBlank() }?.let {
            allUsersList
        } ?: filterApprovalList(value)
    }

    /**
     * Fetches the list of users from the repository and updates [_usersList].
     */
    fun getUsersList() {
        _isDialogVisible.value = true
        /* View Model Scope - Coroutine */
        viewModelScope.launch {
            userRepository.getUsersFromDataSource().run {
                data?.let { result ->
                    allUsersList = result.data
                    _usersList.value = result.data
                } ?: run {
                    _errorMessage.value = error?.error
                }
            }

            /* Hide Progress Dialog with 1 Second delay after fetching the data list from the server */
            _isDialogVisible.value = false
        }
    }

    /**
     * Filters the list of users based on the provided search string.
     *
     * @param searchString The search string entered in the search view.
     * @return The filtered list of users.
     */
    private fun filterApprovalList(searchString: String): List<User>? {
        return allUsersList?.filter { user ->
            with(user) {
                ("$firstName $lastName").lowercase(Locale.getDefault()).contains(
                    searchString.lowercase(
                        Locale.getDefault()
                    )
                )
            }
        }
    }

    /**
     * Fetches the attendance data for a specific user on a given date.
     *
     * @param nic The NIC (National Identity Card) of the user.
     * @param date The date for which attendance is requested.
     */
    fun getTodayAttendanceByUser(nic: String, date: String) {
        _isDialogVisible.value = true
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result =
                attendanceRepository.getTodayAttendanceByUser(
                    nic, date
                )
            _attendanceResult.value = result
            _isDialogVisible.value = false

        }
    }

    /**
     * Sets the attendance data for the ViewModel.
     *
     * @param attendanceData The attendance data to be set.
     */
    fun setAttendanceData(attendanceData: AttendanceData?) {
        _attendanceData.value = attendanceData
    }

    /**
     * Sets the error message for the ViewModel.
     *
     * @param errorMessage The error message to be set.
     */
    fun setErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }
}