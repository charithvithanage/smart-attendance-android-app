package lnbti.charithgtp01.smartattendanceuserapp.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class ResponseWithJSONArray(
    val success: Boolean? = null,
    var message: String? = null,
    var data: JsonArray? = null
)
