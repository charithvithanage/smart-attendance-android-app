package lnbti.charithgtp01.smartattendanceadminapp.model

import org.json.JSONObject

data class LoginResponse(
    val success: Boolean = false,
    var message: String? = null,
    var data: JSONObject? = null
)

