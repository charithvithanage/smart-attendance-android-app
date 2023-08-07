package lnbti.charithgtp01.smartattendanceadminapp.repositories

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lnbti.charithgtp01.smartattendanceadminapp.apiservice.ApiService
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.ErrorBody
import lnbti.charithgtp01.smartattendanceadminapp.model.ErrorResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.Resource
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * User Repository
 */
class UserRepository @Inject constructor(
    context: Context,
    private val userService: ApiService
) {

    val context: Context = context

    /**
     * Login Coroutines
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
     * Change Password Coroutines
     */
    suspend fun changePassword(
        changePasswordRequest: ChangePasswordRequest
    ): ApiCallResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext changePasswordServer(changePasswordRequest)
        }
    }

    /**
     * Send Request to the server and get the response
     */
    private suspend fun changePasswordServer(changePasswordRequest: ChangePasswordRequest): ApiCallResponse {
        val gson = Gson()
        Log.d(TAG, gson.toJson(changePasswordRequest))

        val apiCallResponse: ApiCallResponse
        val response = userService.changePassword(changePasswordRequest)

        apiCallResponse = if (response.isSuccessful) {
            ApiCallResponse(true, response.body().toString())
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            ApiCallResponse(false, errorObject.error)
        }

        return apiCallResponse
    }

    /**
     * Get Pending Approvals from the server
     */
    suspend fun getPendingApprovalsFromDataSource(): Resource {
        return withContext(Dispatchers.IO) {
            return@withContext getPendingApprovalsFromRemoteService()
        }
    }

    /**
     * @param  value: String search view text
     * @return ServerResponse Object
     */
    private suspend fun getPendingApprovalsFromRemoteService(): Resource {

        /* Get Server Response */
        val response = userService.getPendingApprovals()
        return if (response.isSuccessful) {
            Resource.Success(data = response.body()!!)
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            Resource.Error(
                ErrorResponse(
                    errorObject.error,
                    response.code()
                )
            )
        }
    }

    /**
     * Get Users from the server
     */
    suspend fun getUsersFromDataSource(): Resource {
        return withContext(Dispatchers.IO) {
            return@withContext getUsersFromRemoteService()
        }
    }

    /**
     * @return ServerResponse Object
     */
    private suspend fun getUsersFromRemoteService(): Resource {

        /* Get Server Response */
        val response = userService.getPendingApprovals()
        return if (response.isSuccessful) {
            Resource.Success(data = response.body()!!)
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            Resource.Error(
                ErrorResponse(
                    errorObject.error,
                    response.code()
                )
            )
        }
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