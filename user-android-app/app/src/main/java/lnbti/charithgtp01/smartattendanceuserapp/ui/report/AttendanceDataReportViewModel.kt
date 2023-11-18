package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.model.ResponseWithJSONArray
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.AttendanceRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getLastDayOfMonth
import java.util.Date
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class AttendanceDataReportViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) :
    ViewModel() {

    //Date Count Live Date
    private val _dataCountString = MutableLiveData("0")
    val dataCountString get() = _dataCountString

    private val _usersList = MutableLiveData<List<User>>()
    val usersList get() = _usersList

    val selectedUser = MutableLiveData<User>()

    fun onUserSelected(user: User) {
        selectedUser.value = user
    }

    //Start Date Live Date
    private val _startDateString = MutableLiveData<String>()
    val startDateString: LiveData<String> get() = _startDateString

    //End Date Live Date
    private val _endDateString = MutableLiveData<String>()
    val endDateString: LiveData<String> get() = _endDateString

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    //Server response live data
    private val _responseResult = MutableLiveData<ResponseWithJSONArray?>()
    val responseResult: MutableLiveData<ResponseWithJSONArray?> = _responseResult


    /**
     * This will call when the View Model Created
     */
    init {
        val startDate = Date()
        val endDate: Date = getLastDayOfMonth(startDate)
        _startDateString.value = formatDate(startDate)
        _endDateString.value = formatDate(endDate)
    }

    fun setDate(startDate: Date, endDate: Date) {
        _startDateString.value = formatDate(startDate)
        _endDateString.value = formatDate(endDate)
    }

    fun getAttendancesByUser(nic: String, from: String, to: String) {
        _isDialogVisible.value = true
        viewModelScope.launch {
            _responseResult.value = attendanceRepository.getAttendanceDataByUser(nic, from, to)
            _isDialogVisible.value = false
        }
    }

    fun getAttendancesFromAllUsers(from: String, to: String) {
        _isDialogVisible.value = true
        viewModelScope.launch {
            _responseResult.value = attendanceRepository.getAttendances(from, to)
            _isDialogVisible.value = false
        }
    }

    fun setCount(count: Int) {
        _dataCountString.value = count.toString()
    }

    fun setErrorMessage(errorMessage: String) {
        _errorMessage.value = errorMessage.toString()
    }
}