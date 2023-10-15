package lnbti.charithgtp01.smartattendanceuserapp.ui.home

import androidx.lifecycle.LiveData
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
    val usersList: LiveData<List<User>> get() = _usersList

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    lateinit var allUsersList: List<User>

    private var today: Date = Date()

    private val _dateString = MutableLiveData<String>()
    val dateString: LiveData<String> get() = _dateString

    //Live data for Get Attendance Response
    private val _attendanceResult = MutableLiveData<ApiCallResponse?>()
    val attendanceResult: MutableLiveData<ApiCallResponse?> = _attendanceResult

    var name: String? = null
    var attendanceDataString: String? = null
    private val _attendanceData = MutableLiveData<AttendanceData?>()
    val attendanceData: MutableLiveData<AttendanceData?> = _attendanceData

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
        if (value.isNullOrBlank()) {
            _usersList.value = allUsersList
        } else {
            _usersList.value = filterApprovalList(value)
        }
    }

    /**
     * Get Server Response and Set values to live data
     */
    fun getUsersList() {
        if (NetworkUtils.isNetworkAvailable()) {
//Show Progress Dialog when click on the search view submit button
            _isDialogVisible.value = true
            /* View Model Scope - Coroutine */
            viewModelScope.launch {
                val resource = userRepository.getUsersFromDataSource()

                if (resource?.data != null) {
                    allUsersList = resource.data.data
                    _usersList.value = allUsersList
                } else
                    _errorMessage.value = resource?.error?.error

                /* Hide Progress Dialog with 1 Second delay after fetching the data list from the server */
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
    private fun filterApprovalList(searchString: String): List<User>? {
        // to get the result as list
        return allUsersList?.filter { s ->
            (s.firstName + " " + s.lastName).contains(
                searchString
            )
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

}