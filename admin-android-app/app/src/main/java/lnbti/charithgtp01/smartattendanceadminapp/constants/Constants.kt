package lnbti.charithgtp01.smartattendanceadminapp.constants

object Constants {
    const val TAG="Smart Attendance App"

    //Application Base Url
    const val BASE_URL = "http://192.168.1.8:3000/"

    /**
     * Endpoints
     */

    //Login endpoint
    const val LOGIN_ENDPOINT = "admin/login"
    //Get Pending Approval endpoint
    const val GET_PENDING_APPROVALS_ENDPOINT = "user/users"
    //Get Users endpoint
    const val GET_USERS_ENDPOINT = "admin/users"
    //Change Password endpoint
    const val CHANGE_PASSWORD_ENDPOINT = "admin/users/2"
    //Approval endpoint
    const val APPROVAL_ENDPOINT = "admin/users/2"

    /**
     * Preference Keys
     */
    const val ACCESS_TOKEN = "access_token"
    const val LOGGED_IN_USER = "logged in user"

    /**
     * Pass Intent keys
     */
    const val OBJECT_STRING = "object_string"

    /**
     * Dialog Fragment Types
     */
    const val SUCCESS = "success"
    const val FAIL = "fail"
    const val WARN = "warn"
    const val PROGRESS_DIALOG_FRAGMENT_TAG = "CustomProgressDialogFragmentTag"


}