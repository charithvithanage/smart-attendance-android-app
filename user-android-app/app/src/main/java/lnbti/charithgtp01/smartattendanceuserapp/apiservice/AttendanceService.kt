package lnbti.charithgtp01.smartattendanceuserapp.apiservice

import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.CHANGE_PASSWORD_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_COMPANY_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_PENDING_APPROVALS_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_TODAY_ATTENDANCE_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_USERS_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_USER_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.LOGIN_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.MARK_IN_ATTENDANCE_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.MARK_OUT_ATTENDANCE_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.REGISTER_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.RegisterRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ServerJSONResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.ServerResponse
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AttendanceService {
    @POST(MARK_IN_ATTENDANCE_ENDPOINT)
    suspend fun attendanceMarkIn(@Body attendanceMartInRequest: AttendanceMarkInRequest): Response<ApiCallResponse>

    @PUT(MARK_OUT_ATTENDANCE_ENDPOINT)
    suspend fun attendanceMarkOut(@Body attendanceMarkOutRequest: AttendanceMarkOutRequest): Response<ApiCallResponse>

    @GET(GET_TODAY_ATTENDANCE_ENDPOINT)
    suspend fun getTodayAttendanceByUser( @Query("userID") userID: String,
                                     @Query("date") date: String): Response<ApiCallResponse>
}