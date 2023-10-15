package lnbti.charithgtp01.smartattendanceuserapp.ui.searchcompany

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.NO_INTERNET
import lnbti.charithgtp01.smartattendanceuserapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceuserapp.repositories.CompanyRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Validations
import javax.inject.Inject

/**
 * Login Page View Model
 */
@HiltViewModel
class SearchCompanyViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    private val _searchCompanyForm = MutableLiveData<SearchCompanyFormState>()
    val searchCompanyForm: LiveData<SearchCompanyFormState> = _searchCompanyForm

    private val _searchCompanyResult = MutableLiveData<ApiCallResponse?>()
    val searchCompanyResult: MutableLiveData<ApiCallResponse?> = _searchCompanyResult

    var companyID: String? = null


    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible
    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    fun searchCompany() {

        //If Network available call to backend API
        if (NetworkUtils.isNetworkAvailable()) {
            //Show Progress Dialog when click on the search view submit button
            _isDialogVisible.value = true
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result = companyRepository.getCompanyFromID(companyID)
                if (result?.success == true) {
                    _searchCompanyResult.value = result
                }else
                    _errorMessage.value = result?.message

                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = NO_INTERNET
        }
    }

    fun searchCompanyDataChanged() {
        if (!Validations.isCompanyIDValid(companyID)) {
            _searchCompanyForm.value = SearchCompanyFormState(companyIDError = R.string.invalid_company_id)
        }  else {
            _searchCompanyForm.value = SearchCompanyFormState(isDataValid = true)
        }
    }

}