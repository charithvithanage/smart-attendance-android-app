package lnbti.charithgtp01.smartattendanceuserapp.apiservice

import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_COMPANY_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CompanyService {
    @GET("$GET_COMPANY_ENDPOINT{companyID}")
    suspend fun getCompany(@Path("companyID") companyID: String?): Response<ApiCallResponse>
}