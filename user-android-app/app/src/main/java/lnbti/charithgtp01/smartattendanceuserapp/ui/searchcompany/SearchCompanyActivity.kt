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
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.ui.register.RegisterActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBarWithoutHomeButton
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras

@AndroidEntryPoint
class SearchCompanyActivity : AppCompatActivity() {

    private lateinit var searchCompanyViewModel: SearchCompanyViewModel
    private lateinit var binding: ActivitySearchCompanyBinding
    private var dialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initiateDataBinding()
        initiateView()
        viewModelObservers()
    }

    private fun initiateDataBinding() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_search_company)

        //Data binding
        searchCompanyViewModel = ViewModelProvider(this)[SearchCompanyViewModel::class.java]
        binding.vm = searchCompanyViewModel
        binding.lifecycleOwner = this
    }

    private fun initiateView() {
        initiateActionBarWithoutHomeButton(
            binding?.actionBar?.mainLayout!!,
            getString(R.string.search_company)
        ) { onBackPressed() }

        binding.btnSubmit.setOnClickListener {
            searchCompanyViewModel.searchCompanyDataChanged()
        }
    }

    private fun viewModelObservers() {
        /* Show error message in the custom error dialog */
        searchCompanyViewModel.errorMessage.observe(this@SearchCompanyActivity) {
            showErrorDialog(
                this@SearchCompanyActivity,
                it
            )
        }

        searchCompanyViewModel.isDialogVisible.observe(this@SearchCompanyActivity) {
            if (it) {
                /* Show dialog when calling the API */
                dialog = showProgressDialog(
                    this@SearchCompanyActivity,
                    getString(R.string.wait)
                )
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }
        searchCompanyViewModel.searchCompanyForm.observe(this@SearchCompanyActivity, Observer {
            val formState = it ?: return@Observer
            if (formState.companyIDError != null) {
                binding.etCompanyID?.error =
                    getString(formState.companyIDError)
            } else
                validState(binding.companyIDInputText, R.drawable.ic_check)

            if (formState.isDataValid) {
                 searchCompanyViewModel.searchCompany()
            }
        })


        //Waiting for Api response
        searchCompanyViewModel.searchCompanyResult.observe(this@SearchCompanyActivity) {

            val gson = Gson()
            val prefMap = HashMap<String, String>()
            prefMap[Constants.OBJECT_STRING] = gson.toJson(it?.data)
            navigateToAnotherActivityWithExtras(
                this,
                RegisterActivity::class.java,
                prefMap
            )

        }
    }
}

