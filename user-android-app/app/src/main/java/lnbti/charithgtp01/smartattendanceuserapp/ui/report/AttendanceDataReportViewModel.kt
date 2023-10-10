package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceData
import lnbti.charithgtp01.smartattendanceuserapp.model.ResponseWithJSONArray
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.AttendanceRepository
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getLastDayOfMonth
import java.util.Date
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class AttendanceDataReportViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {

    //Users Dropdown values
    var spinnerOptions: Array<String>? = null

    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>> get() = _usersList

    var selectedSpinnerPosition: Int = 0

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
            val result = attendanceRepository.getAttendanceDataByUser(nic, from, to)
            _responseResult.value = result

            //Get all users for the spinner
            val resource = userRepository.getUsersFromDataSource()
            if (resource.data != null) {
                val allUsersList = resource.data.data
                spinnerOptions = allUsersList.map { it.firstName }.toTypedArray()
            }
            delay(2000)
            _isDialogVisible.value = false

        }
    }
}