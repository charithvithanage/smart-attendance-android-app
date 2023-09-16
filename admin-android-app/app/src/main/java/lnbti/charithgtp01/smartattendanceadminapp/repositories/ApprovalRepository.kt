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
     * Approve User Request Coroutines
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
    private suspend fun submitApprovalServer(approvalRequest: ApprovalRequest): ApiCallResponse? {
        val gson = Gson()
        Log.d(TAG, gson.toJson(approvalRequest))

        val response = apiService.submitApproval(approvalRequest)

        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            ApiCallResponse(false, errorObject.message)
        }
    }


    /**
     * Approve User Request Coroutines
     */
    suspend fun rejectApproval(
        nic: String
    ): ApiCallResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext rejectApprovalServer(nic)
        }
    }

    /**
     * Send Request to the server and get the response
     */
    private suspend fun rejectApprovalServer(  nic: String): ApiCallResponse? {
         val response = apiService.rejectApproval(nic)

        return if (response.isSuccessful) {
            response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            ApiCallResponse(false, errorObject.message)
        }
    }


}