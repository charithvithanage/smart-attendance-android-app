package lnbti.charithgtp01.smartattendanceuserapp.model

import org.json.JSONObject

data class ApiCallResponse(
    val success: Boolean? = null,
    var message: String? = null,
    var data: JSONObject? = null
)
