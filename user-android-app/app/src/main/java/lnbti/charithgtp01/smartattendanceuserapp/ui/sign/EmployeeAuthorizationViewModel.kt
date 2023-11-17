package lnbti.charithgtp01.smartattendanceuserapp.ui.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceData
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.AttendanceRepository
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject


@HiltViewModel
class EmployeeAuthorizationViewModel @Inject constructor(private val attendanceRepository: AttendanceRepository) :
    ViewModel() {

    //Server response live data
    private val _attendanceMarkResult = MutableLiveData<ApiCallResponse?>()
    val attendanceMarkResult: MutableLiveData<ApiCallResponse?> = _attendanceMarkResult

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    //Live data for Get Attendance Response
    private val _attendanceResult = MutableLiveData<ApiCallResponse?>()
    val attendanceResult: MutableLiveData<ApiCallResponse?> = _attendanceResult

    fun markIn(attendanceMarkInRequest: AttendanceMarkInRequest) {
        if (NetworkUtils.isNetworkAvailable()) {
            _isDialogVisible.value = true
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result =
                    attendanceRepository.attendanceMarkIn(
                        attendanceMarkInRequest
                    )
                attendanceMarkResult.value = result
                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = ResourceConstants.NO_INTERNET
        }

    }

    fun markOut(attendanceMarkOutRequest: AttendanceMarkOutRequest) {
        if (NetworkUtils.isNetworkAvailable()) {
            _isDialogVisible.value = true
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result =
                    attendanceRepository.attendanceMarkOut(
                        attendanceMarkOutRequest
                    )
                attendanceMarkResult.value = result
                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = ResourceConstants.NO_INTERNET
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
}