package lnbti.charithgtp01.smartattendanceadminapp.apiservice

import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.APPROVAL_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.CHANGE_PASSWORD_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.GET_PENDING_APPROVALS_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.GET_USERS_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.LOGIN_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.REJECT_APPROVAL_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.UPDATE_USER_ENDPOINT
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ApprovalRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.ChangePasswordRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.LoginResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ServerResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.UserUpdateRequest
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @PUT(CHANGE_PASSWORD_ENDPOINT)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<JSONObject>
    @GET(GET_PENDING_APPROVALS_ENDPOINT)
    suspend fun getPendingApprovals(): Response<ServerResponse>

    @GET(GET_USERS_ENDPOINT)
    suspend fun getUsers(): Response<ServerResponse>

    @PUT(APPROVAL_ENDPOINT)
    suspend fun submitApproval(@Body approvalRequest: ApprovalRequest): Response<ApiCallResponse>

    @DELETE("$REJECT_APPROVAL_ENDPOINT{nic}")
    suspend fun rejectApproval(@Path("nic") nic: String): Response<ApiCallResponse>

    @PUT(UPDATE_USER_ENDPOINT)
    suspend fun updateUser(@Body updateRequest: UserUpdateRequest): Response<ApiCallResponse>

}