package lnbti.charithgtp01.smartattendanceadminapp.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lnbti.charithgtp01.smartattendanceadminapp.apiservice.UserService
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import javax.inject.Inject

class UserRepository @Inject constructor(private val userService: UserService) {

    /**
     * Coroutines
     */
    suspend fun login(
        loginRequest: LoginRequest
    ): LoginResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext loginToTheServer(loginRequest)
        }
    }


    /**
     * Send Request to the server and get the response
     */
    private suspend fun loginToTheServer(loginRequest: LoginRequest): LoginResponse? {
        var loginResponse: LoginResponse? = null
        val response = userService.loginUser(loginRequest)
        if (response.isSuccessful) {
            loginResponse = response.body()
        }
        return loginResponse
    }

}