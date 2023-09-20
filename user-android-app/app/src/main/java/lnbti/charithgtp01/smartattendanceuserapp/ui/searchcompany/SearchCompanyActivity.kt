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
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.ui.qr.attendance.AttendanceQRActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.register.RegisterActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils

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
        UIUtils.initiateActionBarWithoutHomeButton(
            binding?.actionBar?.mainLayout!!,
            getString(R.string.search_company)
        ) { onBackPressed() }

        binding.btnSubmit.setOnClickListener {
            searchCompanyViewModel.searchCompanyDataChanged()
        }
    }

    private fun viewModelObservers() {
        searchCompanyViewModel.searchCompanyForm.observe(this@SearchCompanyActivity, Observer {
            val formState = it ?: return@Observer
            if (formState.companyIDError != null) {
                binding.etCompanyID?.error =
                    getString(formState.companyIDError)
            } else
                validState(binding.companyIDInputText, R.drawable.ic_check)

            if (formState.isDataValid) {
                dialog = DialogUtils.showProgressDialog(this, getString(R.string.wait))
                searchCompanyViewModel.searchCompany()
            }
        })


        //Waiting for Api response
        searchCompanyViewModel.searchCompanyResult.observe(this@SearchCompanyActivity) {
            val apiResult = it

            dialog?.dismiss()

            if (apiResult?.success == true) {
                val gson = Gson()
                val prefMap = HashMap<String, String>()
                prefMap[Constants.OBJECT_STRING] = gson.toJson(apiResult.data)
                Utils.navigateToAnotherActivityWithExtras(
                    this,
                    RegisterActivity::class.java,
                    prefMap
                )
            } else {
                DialogUtils.showAlertDialog(this,Constants.FAIL, apiResult?.message,object : CustomAlertDialogListener{
                    override fun onDialogButtonClicked() {

                    }

                })
            }

        }
    }
}

