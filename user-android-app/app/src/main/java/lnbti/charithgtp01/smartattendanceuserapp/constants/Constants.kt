package lnbti.charithgtp01.smartattendanceuserapp.constants

import android.Manifest

object Constants {


    const val TAG = "Smart Attendance App"

    //Application Base Url
//    const val BASE_URL = "https://reqres.in/api/"
//    const val BASE_URL = "http://192.168.115.120:3000/"
    const val BASE_URL = "https://ob297ofh21.execute-api.us-east-1.amazonaws.com/"

    /**
     * Endpoints
     */

    //Login endpoint
    const val LOGIN_ENDPOINT = "user/login"

    //Get Pending Approval endpoint
    const val GET_PENDING_APPROVALS_ENDPOINT = "users"

    //Get Users endpoint
    const val GET_USERS_ENDPOINT = "user/users"

    //Change Password endpoint
    const val CHANGE_PASSWORD_ENDPOINT = "user/change-password"

    //User Register endpoint
    const val REGISTER_ENDPOINT = "user/register"

    //Get User endpoint
    const val GET_USER_ENDPOINT = "users/5"

    //Mark In Attendance Endpoint
    const val MARK_IN_ATTENDANCE_ENDPOINT = "attendance/in"

    //Mark Out Attendance Endpoint
    const val MARK_OUT_ATTENDANCE_ENDPOINT = "attendance/out"

    //Get Today Attendance Endpoint
    const val GET_TODAY_ATTENDANCE_ENDPOINT = "attendance/today-attendancebyuser"

    //Get Attendance By NameEndpoint
    const val GET_ATTENDANCES_BY_USER_ENDPOINT = "attendance/attendancesbyuser"

    //Get Attendance By Date Range Endpoint
    const val GET_ATTENDANCE_ENDPOINT = "attendance/attendances"

    /**
     * Company Endpoints
     */
    //Get User endpoint
    const val GET_COMPANY_ENDPOINT = "company/companies/"

    /**
     * Preference Keys
     */
    const val LOGGED_IN_USER = "logged in user"
    const val ACCESS_TOKEN = "access_token"
    const val USER_ROLE = "user_role"
    const val USERS_LIST = "users_list"

    /**
     * Pass Intent keys
     */
    const val OBJECT_STRING = "object_string"
    const val ATTENDANCE_TYPE = "Attendance Type"

    /**
     * Permission Codes
     */
    var SCANNER_PERMISSIONS: Array<String> = arrayOf(
        Manifest.permission.CAMERA
    )
    const val PERMISSION_ALL = 1

    /**
     * Image Types
     */
    const val MEDIA_TYPE_EMPLOYEE_SIGNATURE: String = "employee_signature"

    /**
     * Dialog Fragment Types
     */
    const val SUCCESS = "success"
    const val FAIL = "fail"
    const val WARN = "warn"
    const val PROGRESS_DIALOG_FRAGMENT_TAG = "CustomProgressDialogFragmentTag"

    /**
     * Encryption Secure Key
     */
    const val SECURE_KEY = "12345"

}