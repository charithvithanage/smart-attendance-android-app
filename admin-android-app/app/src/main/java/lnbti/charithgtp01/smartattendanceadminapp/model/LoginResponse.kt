package lnbti.charithgtp01.smartattendanceadminapp.model

import com.google.gson.JsonObject
import org.json.JSONObject

data class LoginResponse(
    val success: Boolean = false,
    var message: String? = null,
    var data: JsonObject? = null
)

