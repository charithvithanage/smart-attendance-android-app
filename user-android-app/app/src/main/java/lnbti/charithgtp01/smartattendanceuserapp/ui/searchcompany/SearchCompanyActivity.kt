package lnbti.charithgtp01.smartattendanceuserapp.ui.searchcompany

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivitySearchCompanyBinding
import lnbti.charithgtp01.smartattendanceuserapp.ui.register.RegisterActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBarWithoutHomeButton
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras

/**
 * Activity for searching companies.
 *
 * This activity is responsible for allowing users to search for companies.
 *
 * @property searchCompanyViewModel ViewModel for managing data related to company search.
 * @property binding Data binding for the activity.
 * @property dialog Dialog fragment for showing progress or error messages.
 */
@AndroidEntryPoint
class SearchCompanyActivity : AppCompatActivity() {

    private lateinit var searchCompanyViewModel: SearchCompanyViewModel
    private lateinit var binding: ActivitySearchCompanyBinding
    private var dialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        viewModelObservers()
    }

    /**
     * Initializes data binding for the activity.
     */
    private fun initiateDataBinding() {

        //Data binding
        ViewModelProvider(this)[SearchCompanyViewModel::class.java].apply {
            searchCompanyViewModel =this
            binding =
                DataBindingUtil.setContentView<ActivitySearchCompanyBinding?>(this@SearchCompanyActivity, R.layout.activity_search_company).apply {
                    vm = searchCompanyViewModel
                    lifecycleOwner = this@SearchCompanyActivity

                    initiateActionBarWithoutHomeButton(
                        actionBar.mainLayout,
                        getString(R.string.search_company)
                    ) { onBackPressed() }

                    btnSubmit.setOnClickListener {
                        searchCompanyDataChanged()
                    }
                }
        }

    }

    /**
     * Observes changes in the ViewModel and takes appropriate actions.
     */
    private fun viewModelObservers() {
        searchCompanyViewModel.apply {
            // Handle error messages
            errorMessage.observe(this@SearchCompanyActivity) {
                showErrorDialog(
                    this@SearchCompanyActivity,
                    it
                )
            }

            // Show or dismiss progress dialog
            isDialogVisible.observe(this@SearchCompanyActivity) {
                when {
                    it -> dialog = showProgressDialog(
                        this@SearchCompanyActivity,
                        getString(R.string.wait)
                    )
                    else -> dialog?.dismiss()
                }
                // Show dialog when calling the API
                // Dismiss dialog after updating the data list to recycle view
            }

            // Observe form state for searchCompanyForm
            searchCompanyForm.observe(this@SearchCompanyActivity, Observer {
                val formState = it ?: return@Observer

                formState.apply {
                    if (companyIDError != null) binding.etCompanyID.error =
                        getString(companyIDError) else
                        validState(binding.companyIDInputText, R.drawable.ic_check)

                    isDataValid.let {
                        // If Network available, call the backend API
                        when {
                            NetworkUtils.isNetworkAvailable() -> searchCompany()
                            else -> setErrorMessage(getString(R.string.no_internet))
                        }
                    }
                }

            })


            // Waiting for API response
            searchCompanyResult.observe(this@SearchCompanyActivity) {
                Gson().apply {
                    HashMap<String, String>().apply {
                        this[Constants.OBJECT_STRING] = toJson(it?.data)
                        navigateToAnotherActivityWithExtras(
                            this@SearchCompanyActivity,
                            RegisterActivity::class.java,
                            this
                        )
                    }
                }
            }
        }

    }
}

