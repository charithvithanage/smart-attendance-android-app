package lnbti.charithgtp01.smartattendanceuserapp.repositories

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lnbti.charithgtp01.smartattendanceuserapp.apiservice.UserService
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ErrorBody
import lnbti.charithgtp01.smartattendanceuserapp.model.ErrorResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.RegisterRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.Resource
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getErrorBodyFromResponse
import javax.inject.Inject

/**
 * User Repository
 */
class UserRepository @Inject constructor(
    private val userService: UserService
) {

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
        val gson = Gson()
        Log.d(TAG, gson.toJson(loginRequest))

        return try {
            val response = userService.loginUser(loginRequest)
            Log.d(TAG, "Response Object " + response.body().toString())
            if (response.isSuccessful) {
                response.body()
            } else {
                val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
                LoginResponse(false, errorObject.message)
            }
        } catch (e: Exception) {
            LoginResponse(false, e.message)
        }
    }

    /**
     * Register Coroutines
     */
    suspend fun register(
        registerRequest: RegisterRequest
    ): ApiCallResponse {
        return withContext(Dispatchers.IO) {
            return@withContext registerServer(registerRequest)
        }
    }

    /**
     * Send Request to the server and get the response
     */
    private suspend fun registerServer(registerRequest: RegisterRequest): ApiCallResponse {
        val gson = Gson()
        Log.d(TAG, gson.toJson(registerRequest))

        return try {
            val response = userService.register(registerRequest)

            if (response.isSuccessful) {
                ApiCallResponse(true, response.body().toString())
            } else {
                val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
                ApiCallResponse(false, errorObject.message)
            }
        } catch (e: Exception) {
            ApiCallResponse(false, e.message)
        }
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
    private suspend fun changePasswordServer(changePasswordRequest: ChangePasswordRequest): ApiCallResponse? {
        val response = userService.changePassword(changePasswordRequest)
        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            ApiCallResponse(false, errorObject.message)
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

        return try {
            /* Get Server Response */
            val response = userService.getUsers()
            return if (response.isSuccessful) {
                Resource.Success(data = response.body()!!)
            } else {
                val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
                Resource.Error(
                    ErrorResponse(
                        errorObject.message,
                        response.code()
                    )
                )
            }
        } catch (e: Exception) {
            Resource.Error(
                ErrorResponse(
                    e.toString(),
                    404
                )
            )
        }

    }
}