package lnbti.charithgtp01.smartattendanceadminapp.constants

object Constants {
    const val TAG="Smart Attendance Admin App"

    //Application Base Url
//    const val BASE_URL = "http://192.168.1.22:3000/"
    const val BASE_URL = "https://ob297ofh21.execute-api.us-east-1.amazonaws.com/"

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
    const val CHANGE_PASSWORD_ENDPOINT = "admin/change-password"
    //Approval endpoint
    const val APPROVAL_ENDPOINT = "user/activateUser"
    //Reject Approval endpoint
    const val REJECT_APPROVAL_ENDPOINT = "user/deleteUser/"
    //Update user endpoint
    const val UPDATE_USER_ENDPOINT = "user/updateUser"

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