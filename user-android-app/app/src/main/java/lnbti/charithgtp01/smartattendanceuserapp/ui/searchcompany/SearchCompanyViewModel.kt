package lnbti.charithgtp01.smartattendanceuserapp.ui.searchcompany

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.repositories.CompanyRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations
import javax.inject.Inject

/**
 * ViewModel class for searching company information.
 *
 * This class is annotated with @HiltViewModel, indicating that it should be constructed
 * and provided by the Hilt dependency injection framework.
 *
 * @property companyRepository The repository responsible for handling company data.
 * @property _searchCompanyForm LiveData representing the state of the company search form.
 * @property searchCompanyForm Publicly accessible LiveData for observing the company search form state.
 * @property _searchCompanyResult LiveData representing the result of the company search API call.
 * @property searchCompanyResult Publicly accessible LiveData for observing the company search API call result.
 * @property companyID The ID of the company being searched.
 * @property _isDialogVisible LiveData representing the visibility state of the progress dialog.
 * @property isDialogVisible Publicly accessible LiveData for observing the progress dialog visibility.
 * @property _errorMessage LiveData representing the error message during the company search.
 * @property errorMessage Publicly accessible LiveData for observing the error message during the company search.
 */
@HiltViewModel
class SearchCompanyViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val _searchCompanyForm = MutableLiveData<SearchCompanyFormState>()
    val searchCompanyForm: LiveData<SearchCompanyFormState> = _searchCompanyForm

    private val _searchCompanyResult = MutableLiveData<ApiCallResponse?>()
    val searchCompanyResult: MutableLiveData<ApiCallResponse?> = _searchCompanyResult

    var companyID: String? = "SA00001"


    // Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    // Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    /**
     * Initiates the company search process.
     */
    fun searchCompany() {
        // Show Progress Dialog when clicking on the search view submit button
        _isDialogVisible.value = true
        viewModelScope.launch {
            // Can be launched in a separate asynchronous job
            val result = companyRepository.getCompanyFromID(companyID)

            result?.success?.run {
                when {
                    this -> _searchCompanyResult.value = result
                    else -> _errorMessage.value = result.message
                }
            }

            _isDialogVisible.value = false
        }
    }

    /**
     * Sets the error message for the company search.
     *
     * @param errorMessage The error message to be set.
     */
    fun setErrorMessage(errorMessage: String) {
        _errorMessage.value = errorMessage
    }

    /**
     * Function to handle changes in search company data.
     */
    fun searchCompanyDataChanged() {
        _searchCompanyForm.apply {
            value = when {
                !Validations.isCompanyIDValid(companyID) -> SearchCompanyFormState(companyIDError = R.string.invalid_company_id)
                else -> SearchCompanyFormState(isDataValid = true)
            }
        }
    }

}