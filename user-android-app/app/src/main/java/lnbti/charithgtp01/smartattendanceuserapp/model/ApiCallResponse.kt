package lnbti.charithgtp01.smartattendanceuserapp.model

import com.google.gson.JsonObject

data class ApiCallResponse(
    val success: Boolean? = null,
    var message: String? = null,
    var data: JsonObject? = null
)
