package lnbti.charithgtp01.smartattendanceadminapp.apiservice

import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.LOGIN_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
}