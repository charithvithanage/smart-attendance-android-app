package lnbti.charithgtp01.smartattendanceuserapp.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.AttendanceRepository
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations.Companion.isPasswordValid
import javax.inject.Inject


@HiltViewModel
class ScanViewModel @Inject constructor(private val attendanceRepository: AttendanceRepository) :
    ViewModel() {

    //Server response live data
    private val _attendanceMarkResult = MutableLiveData<ApiCallResponse?>()
    val attendanceMarkResult: MutableLiveData<ApiCallResponse?> = _attendanceMarkResult


    fun markIn(attendanceMarkInRequest: AttendanceMarkInRequest) {
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result =
                attendanceRepository.attendanceMarkIn(
                    attendanceMarkInRequest
                )
            attendanceMarkResult.value = result
        }
    }

    fun markOut(attendanceMarkOutRequest: AttendanceMarkOutRequest) {
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result =
                attendanceRepository.attendanceMarkOut(
                    attendanceMarkOutRequest
                )
            attendanceMarkResult.value = result
        }
    }

}