package lnbti.charithgtp01.smartattendanceuserapp.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lnbti.charithgtp01.smartattendanceuserapp.apiservice.CompanyService
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.ErrorBody
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getErrorBodyFromResponse
import javax.inject.Inject

/**
 * User Repository
 */
class CompanyRepository @Inject constructor(
    private val companyService: CompanyService
) {
    /**
     * Get Company from the server
     */
    suspend fun getCompanyFromID(companyID:String?): ApiCallResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext getCompanyFromRemoteService(companyID)
        }
    }


    /**
     * @return ServerResponse Object
     */
    private suspend fun getCompanyFromRemoteService(companyID: String?): ApiCallResponse? {
        var apiResponse: ApiCallResponse? = ApiCallResponse()

        val response = companyService.getCompany(companyID)
        if (response.isSuccessful) {
            apiResponse = response.body()
        } else {
            val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
            apiResponse?.message = errorObject.message
        }
        return apiResponse

    }



}