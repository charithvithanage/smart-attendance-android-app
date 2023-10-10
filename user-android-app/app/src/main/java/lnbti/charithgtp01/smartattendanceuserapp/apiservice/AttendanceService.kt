package lnbti.charithgtp01.smartattendanceuserapp.apiservice

import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_ATTENDANCES_BY_USER_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_ATTENDANCE_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_TODAY_ATTENDANCE_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.MARK_IN_ATTENDANCE_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.MARK_OUT_ATTENDANCE_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ResponseWithJSONArray
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AttendanceService {
    @POST(MARK_IN_ATTENDANCE_ENDPOINT)
    suspend fun attendanceMarkIn(@Body attendanceMartInRequest: AttendanceMarkInRequest): Response<ApiCallResponse>

    @PUT(MARK_OUT_ATTENDANCE_ENDPOINT)
    suspend fun attendanceMarkOut(@Body attendanceMarkOutRequest: AttendanceMarkOutRequest): Response<ApiCallResponse>

    @GET(GET_TODAY_ATTENDANCE_ENDPOINT)
    suspend fun getTodayAttendanceByUser(
        @Query("userID") userID: String,
        @Query("date") date: String
    ): Response<ApiCallResponse>

    @GET(GET_ATTENDANCES_BY_USER_ENDPOINT)
    suspend fun getAttendanceByUser(
        @Query("userID") userID: String,
        @Query("fromDate") fromDate: String, @Query("toDate") toDate: String
    ): Response<ResponseWithJSONArray>

    @GET(GET_ATTENDANCE_ENDPOINT)
    suspend fun getAttendances(
        @Query("fromDate") fromDate: String, @Query("toDate") toDate: String
    ): Response<ResponseWithJSONArray>
}