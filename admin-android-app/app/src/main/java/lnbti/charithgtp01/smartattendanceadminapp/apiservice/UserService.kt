package lnbti.charithgtp01.smartattendanceadminapp.apiservice

import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.GET_PENDING_APPROVALS_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.LOGIN_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ServerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
   @GET(GET_PENDING_APPROVALS_ENDPOINT)
    suspend fun getPendingApprovals(): Response<ServerResponse>
}