package lnbti.charithgtp01.smartattendanceadminapp.repositories

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lnbti.charithgtp01.smartattendanceadminapp.apiservice.ApiService
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ApprovalRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.ErrorBody
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.getErrorBodyFromResponse
import javax.inject.Inject

/**
 * User Repository
 */
class ApprovalRepository @Inject constructor(
    context: Context,
    private val apiService: ApiService
) {

    val context: Context = context


    /**
     * Change Password Coroutines
     */
    suspend fun submitApproval(
        approvalRequest: ApprovalRequest
    ): ApiCallResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext submitApprovalServer(approvalRequest)
        }
    }

    /**
     * Send Request to the server and get the response
     */
    private suspend fun submitApprovalServer(approvalRequest: ApprovalRequest): ApiCallResponse {
        val gson = Gson()
        Log.d(TAG, gson.toJson(approvalRequest))

        val apiCallResponse: ApiCallResponse
        val response = apiService.submitApproval(approvalRequest)

        apiCallResponse = if (response.isSuccessful) {
            ApiCallResponse(true, response.body().toString())
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            ApiCallResponse(false, errorObject.message)
        }

        return apiCallResponse
    }


}