package lnbti.charithgtp01.smartattendanceuserapp.repositories

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lnbti.charithgtp01.smartattendanceuserapp.apiservice.AttendanceService
import lnbti.charithgtp01.smartattendanceuserapp.apiservice.UserService
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ErrorBody
import lnbti.charithgtp01.smartattendanceuserapp.model.ErrorResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.JSONResource
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.RegisterRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.Resource
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getErrorBodyFromResponse
import javax.inject.Inject

/**
 * User Repository
 */
class AttendanceRepository @Inject constructor(
    private val attendanceService: AttendanceService
) {

    /**
     * Attendance Mark In Coroutines
     */
    suspend fun attendanceMarkIn(
        attendanceMarkInRequest: AttendanceMarkInRequest
    ): ApiCallResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext attendanceMarkInToTheServer(attendanceMarkInRequest)
        }
    }

    /**
     * Send Request to the server and get the response
     */
    private suspend fun attendanceMarkInToTheServer(attendanceMarkInRequest: AttendanceMarkInRequest): ApiCallResponse? {
        val gson=Gson()
        Log.d(TAG, "Request Object " + gson.toJson(attendanceMarkInRequest))
        val response = attendanceService.attendanceMarkIn(attendanceMarkInRequest)
        Log.d(TAG, "Response Object " + response.body().toString())

        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            ApiCallResponse(false, errorObject.message)
        }
    }


    /**
     * Attendance Mark Out Coroutines
     */
    suspend fun attendanceMarkOut(
        attendanceMarkOutRequest: AttendanceMarkOutRequest
    ): ApiCallResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext attendanceMarkOutToServer(attendanceMarkOutRequest)
        }
    }

    /**
     * Send Request to the server and get the response
     */
    private suspend fun attendanceMarkOutToServer(attendanceMarkOutRequest: AttendanceMarkOutRequest): ApiCallResponse? {
        val response = attendanceService.attendanceMarkOut(attendanceMarkOutRequest)
        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            ApiCallResponse(false, errorObject.message)
        }
    }
}