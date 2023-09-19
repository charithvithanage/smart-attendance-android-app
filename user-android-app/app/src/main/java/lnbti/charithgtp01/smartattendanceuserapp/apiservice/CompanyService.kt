package lnbti.charithgtp01.smartattendanceuserapp.apiservice

import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.CHANGE_PASSWORD_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_COMPANY_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_PENDING_APPROVALS_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_USERS_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.GET_USER_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.LOGIN_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.REGISTER_ENDPOINT
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.RegisterRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.ServerJSONResponse
import lnbti.charithgtp01.smartattendanceuserapp.model.ServerResponse
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CompanyService {
    @GET("$GET_COMPANY_ENDPOINT{companyID}")
    suspend fun getCompany(@Path("companyID") companyID: String?): Response<ApiCallResponse>
}