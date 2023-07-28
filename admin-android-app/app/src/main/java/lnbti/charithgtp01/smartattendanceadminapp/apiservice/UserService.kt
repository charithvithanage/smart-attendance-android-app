package lnbti.charithgtp01.smartattendanceadminapp.apiservice

import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.CHANGE_PASSWORD_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.GET_PENDING_APPROVALS_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.GET_USERS_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.LOGIN_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ServerResponse
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @PUT(CHANGE_PASSWORD_ENDPOINT)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<JSONObject>
    @GET(GET_PENDING_APPROVALS_ENDPOINT)
    suspend fun getPendingApprovals(): Response<ServerResponse>

    @GET(GET_USERS_ENDPOINT)
    suspend fun getUsers(): Response<ServerResponse>
}