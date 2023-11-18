package lnbti.charithgtp01.smartattendanceuserapp.ui.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.repositories.AttendanceRepository
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val attendanceRepository: AttendanceRepository) :
    ViewModel() {

    //Server response live data
    private val _attendanceMarkResult = MutableLiveData<ApiCallResponse?>()
    val attendanceMarkResult = _attendanceMarkResult

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage
    fun markIn(attendanceMarkInRequest: AttendanceMarkInRequest) {
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
    }

    fun markOut(attendanceMarkOutRequest: AttendanceMarkOutRequest) {
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
    }

    fun setErrorMessage(errorMessage: String) {
        _errorMessage.value = errorMessage
    }

}