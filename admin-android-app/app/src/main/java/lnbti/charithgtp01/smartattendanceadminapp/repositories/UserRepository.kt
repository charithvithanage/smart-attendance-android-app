package lnbti.charithgtp01.smartattendanceadminapp.repositories

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lnbti.charithgtp01.smartattendanceadminapp.apiservice.UserService
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceadminapp.model.ErrorBody
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import okhttp3.ResponseBody
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
        var loginResponse: LoginResponse? = LoginResponse()

        val gson = Gson()
        Log.d(TAG, gson.toJson(loginRequest))

        val response = userService.loginUser(loginRequest)
        if (response.isSuccessful) {
            loginResponse = response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            loginResponse?.error = errorObject.error
        }
        return loginResponse
    }

    /**
     * Deserialize error response.body
     * @param errorBody Error Response
     */
    private fun getErrorBodyFromResponse(errorBody: ResponseBody?): ErrorBody {
        Log.d(TAG, errorBody.toString())
        val gson = Gson()
        val type = object : TypeToken<ErrorBody>() {}.type
        return gson.fromJson(errorBody?.charStream(), type)
    }

}