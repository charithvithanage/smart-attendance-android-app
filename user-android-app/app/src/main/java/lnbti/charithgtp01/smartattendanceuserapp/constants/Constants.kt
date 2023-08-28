package lnbti.charithgtp01.smartattendanceuserapp.constants

import android.Manifest

object Constants {

    const val TAG="Smart Attendance App"

    //Application Base Url
//    const val BASE_URL = "https://reqres.in/api/"
    const val BASE_URL = "http://192.168.1.8:3000/"

    /**
     * Endpoints
     */

    //Login endpoint
    const val LOGIN_ENDPOINT = "user/login"
    //Get Pending Approval endpoint
    const val GET_PENDING_APPROVALS_ENDPOINT = "users"
    //Get Users endpoint
    const val GET_USERS_ENDPOINT = "users"
    //Change Password endpoint
    const val CHANGE_PASSWORD_ENDPOINT = "users/2"
    //User Register endpoint
    const val REGISTER_ENDPOINT = "users/2"
    //Get User endpoint
    const val GET_USER_ENDPOINT = "users/5"

    /**
     * Preference Keys
     */
    const val LOGGED_IN_USER = "logged in user"
    const val ACCESS_TOKEN = "access_token"
    const val USER_ROLE="user_role"

    /**
     * Pass Intent keys
     */
    const val OBJECT_STRING = "object_string"

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