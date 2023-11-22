package lnbti.charithgtp01.smartattendanceuserapp.ui.searchcompany

/**
 * Represents the state of a search form for a company.
 *
 * @property companyIDError The error code associated with the company ID input. Set to `null` if there is no error.
 * @property isDataValid Indicates whether the form data is valid or not. Default is `false`.
 *
 */
data class SearchCompanyFormState(
    val companyIDError: Int? = null,
    val isDataValid: Boolean = false
)