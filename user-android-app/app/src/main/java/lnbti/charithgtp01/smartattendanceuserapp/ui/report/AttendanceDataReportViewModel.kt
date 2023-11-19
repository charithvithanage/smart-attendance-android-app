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
) : ViewModel() {

    //Date Count Live Date
    private val _dataCountString = MutableLiveData("0")
    val dataCountString get() = _dataCountString

    val selectedUser = MutableLiveData<User>()

    fun onUserSelected(user: User) {
        selectedUser.value = user
    }

    //Start Date Live Date
    private val _startDate = MutableLiveData(Date())
    val startDate: LiveData<Date> get() = _startDate

    //End Date Live Date
    private val _endDate = MutableLiveData(getLastDayOfMonth(Date()))
    val endDate: LiveData<Date> get() = _endDate

    private val _startDateString = MutableLiveData(formatDate(Date()))
    val startDateString: LiveData<String> get() = _startDateString

    //End Date Live Date
    private val _endDateString = MutableLiveData(formatDate(getLastDayOfMonth(Date())))
    val endDateString: LiveData<String> get() = _endDateString

    //Server response live data
    private val _responseResult = MutableLiveData<ResponseWithJSONArray?>()
    val responseResult: MutableLiveData<ResponseWithJSONArray?> = _responseResult

    fun setStartDate(startDate: Date) {
        _startDate.value = startDate
        _startDateString.value = formatDate(startDate)
    }

    fun setEndDate(endDate: Date) {
        _endDate.value = endDate
        _endDateString.value = formatDate(endDate)
    }

    fun getAttendancesByUser(nic: String) {
        viewModelScope.launch {
            _responseResult.value =
                attendanceRepository.getAttendanceDataByUser(nic, startDateString.value, endDateString.value)
        }
    }

    fun getAttendancesFromAllUsers() {
        viewModelScope.launch {
            _responseResult.value =
                attendanceRepository.getAttendances(startDateString.value, endDateString.value)
        }
    }

    fun setCount(count: Int) {
        _dataCountString.value = count.toString()
    }
}