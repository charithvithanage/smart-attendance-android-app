package lnbti.charithgtp01.smartattendanceuserapp.model

/**
 * Sealed class to catch both Success and Error Response from Server
 */
sealed class JSONResource(
    val data: ServerJSONResponse? = null,
    val error: ErrorResponse? = null
) {
    // We'll wrap our data in this 'Success'
    // class in case of success response from api
    class Success(data: ServerJSONResponse) : JSONResource(data = data)
    // We'll pass error message wrapped in this 'Error'
    // class to the UI in case of failure response
    class Error(error: ErrorResponse) : JSONResource(error = error)

}
