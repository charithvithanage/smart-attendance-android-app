package lnbti.charithgtp01.smartattendanceuserapp.ui.changepassword

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityChangePasswordBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private lateinit var binding: ActivityChangePasswordBinding
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initiateDataBinding()
        initiateView()
        initiateProgressDialog()
        viewModelObservers()
    }

    private fun initiateDataBinding() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_change_password)

        //Data binding
        changePasswordViewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        binding.vm = changePasswordViewModel
        binding.lifecycleOwner = this
    }

    private fun initiateView() {
        UIUtils.initiateActionBar(
            binding?.actionBar?.mainLayout!!,
            getString(R.string.change_password),
            object : ActionBarListener {
                override fun backPressed() {
                    onBackPressed()
                }

                override fun homePressed() {
                    Utils.goToHomeActivity(this@ChangePasswordActivity)
                }
            })

        binding.btnSubmit.setOnClickListener {
            changePasswordViewModel.validateFields()
        }
    }

    private fun viewModelObservers() {
        //If all fields are correct call the change password api
        changePasswordViewModel.changePasswordForm.observe(this@ChangePasswordActivity, Observer {
            val formState = it ?: return@Observer

            if (formState.currentPasswordError != null) {
                binding.currentPasswordInputText?.error =
                    getString(formState.currentPasswordError)
            } else
                validState(binding.currentPasswordInputText, R.drawable.ic_check)

            if (formState.newPasswordError != null) {
                binding.newPasswordInputText?.error =
                    getString(formState.newPasswordError)
            } else
                validState(binding.newPasswordInputText, R.drawable.ic_check)

            if (formState.confirmPasswordError != null) {
                binding.confirmPasswordInputText?.error =
                    getString(formState.confirmPasswordError)
            } else
                validState(binding.confirmPasswordInputText, R.drawable.ic_check)

            if (formState.isDataValid) {
                dialog?.show()
                changePasswordViewModel.changePassword()
            }
        })


        //Waiting for Api response
        changePasswordViewModel.changePasswordResult.observe(this@ChangePasswordActivity) {
            val apiResult = it

            dialog?.dismiss()

            if (apiResult?.success == true) {
                showAlertDialog(
                    this,
                    getString(R.string.password_changed_successfully),
                    object : DialogButtonClickListener {
                        override fun onButtonClick() {
                            onBackPressed()
                        }

                    })
            } else if (apiResult?.data != null) {
                showErrorDialog(this, apiResult?.data, object : DialogButtonClickListener {
                    override fun onButtonClick() {

                    }
                })

            }

        }
    }

    /**
     * Progress Dialog Initiation
     */
    private fun initiateProgressDialog() {
        dialog = showProgressDialog(this, getString(R.string.wait))
    }
}
