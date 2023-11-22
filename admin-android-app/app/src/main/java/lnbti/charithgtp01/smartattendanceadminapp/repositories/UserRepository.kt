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
import lnbti.charithgtp01.smartattendanceadminapp.model.ApprovalRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.ErrorBody
import lnbti.charithgtp01.smartattendanceadminapp.model.ErrorResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.Resource
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.model.UserUpdateRequest
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.getErrorBodyFromResponse
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * User Repository
 */
class UserRepository @Inject constructor(
    private val userService: ApiService
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

        val response = userService.loginUser(loginRequest)
        Log.d(TAG, "Response Object "+response.body().toString())

        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            LoginResponse(false, errorObject.message)
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
                    errorObject.message,
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
                    errorObject.message,
                    response.code()
                )
            )
        }
    }

    /**
     * Update User Request Coroutines
     */
    suspend fun updateUser(
        updateRequest: UserUpdateRequest
    ): ApiCallResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext updateUserServer(updateRequest)
        }
    }

    /**
     * Send Request to the server and get the response
     */
    private suspend fun updateUserServer(updateRequest: UserUpdateRequest): ApiCallResponse? {
        val response = userService.updateUser(updateRequest)

        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            ApiCallResponse(false, errorObject.message)
        }
    }
}