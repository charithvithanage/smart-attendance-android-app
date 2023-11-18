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
 * Users Fragment View Model
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
     * This will call when the View Model Created
     */
    init {
        _dateString.value = formatDate(today)
    }

    /**
     * Search View on text change listener
     * @param searchString Entering value
     */
    fun onSearchViewTextChanged(searchString: CharSequence?) {
        val value = searchString.toString()
        _usersList.value = value.takeIf { it.isBlank() }?.let {
            allUsersList
        } ?: filterApprovalList(value)
    }

    /**
     * Get Server Response and Set values to live data
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
     * @param searchString Search View entered value
     * @return Data list filtered by user's full name
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
     * Get attendance data from nic and date
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

    fun setAttendanceData(attendanceData: AttendanceData?) {
        _attendanceData.value = attendanceData
    }

    fun setErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }

}