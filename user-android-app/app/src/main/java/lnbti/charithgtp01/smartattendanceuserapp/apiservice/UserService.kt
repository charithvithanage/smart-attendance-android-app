package lnbti.charithgtp01.smartattendanceuserapp.apiservice

import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.CHANGE_PASSWORD_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_PENDING_APPROVALS_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_USERS_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_USER_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.LOGIN_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.REGISTER_ENDPOINT
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

interface UserService {
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @PUT(REGISTER_ENDPOINT)
    suspend fun register(@Body registerRequest: RegisterRequest): Response<JSONObject>
    @PUT(CHANGE_PASSWORD_ENDPOINT)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<JSONObject>
    @GET(GET_USER_ENDPOINT)
    suspend fun getUser(): Response<ServerJSONResponse>
    @GET(GET_USERS_ENDPOINT)
    suspend fun getUsers(): Response<ServerResponse>
}