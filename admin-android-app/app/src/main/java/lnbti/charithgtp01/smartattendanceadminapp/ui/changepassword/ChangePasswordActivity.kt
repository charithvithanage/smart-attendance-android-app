package lnbti.charithgtp01.smartattendanceadminapp.ui.changepassword

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.LOGGED_IN_USER
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.PROGRESS_DIALOG_FRAGMENT_TAG
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityChangePasswordBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils
import okhttp3.internal.Util

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private lateinit var binding: ActivityChangePasswordBinding
    private var dialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initiateDataBinding()
        initiateView()
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

    lateinit var loggedInUser: User
    private fun initiateView() {

        UIUtils.changeUiSize(this@ChangePasswordActivity, binding.iconView, 2, 3)

        val gson = Gson()
        val loggedInUserString = Utils.getObjectFromSharedPref(this, LOGGED_IN_USER)
        loggedInUser = gson.fromJson(loggedInUserString, User::class.java)

        initiateActionBar(
            binding.actionBar.mainLayout,
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

        changePasswordViewModel.setFocusChangeListener(binding.etCurrentPassword)
        changePasswordViewModel.setFocusChangeListener(binding.etNewPassword)
        changePasswordViewModel.setFocusChangeListener(binding.etConfirmPassword)
//
//        changePasswordViewModel.currentPassword="Charith@123"
//        changePasswordViewModel.newPassword="Charith@1234"
//        changePasswordViewModel.confirmPassword="Charith@1234"
    }

    private fun viewModelObservers() {
        /* Show error message in the custom error dialog */
        changePasswordViewModel.errorMessage.observe(this@ChangePasswordActivity) {
            showErrorDialog(this@ChangePasswordActivity, it)
        }

        changePasswordViewModel.isDialogVisible.observe(this@ChangePasswordActivity) {
            if (it) {
                /* Show dialog when calling the API */
                dialog = showProgressDialog(this@ChangePasswordActivity, getString(R.string.wait))
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }

        //If all fields are correct call the change password api
        changePasswordViewModel.changePasswordForm.observe(this@ChangePasswordActivity, Observer {
            val formState = it ?: return@Observer

            if (formState.currentPasswordError != null) {
                binding.currentPasswordInputText.error =
                    getString(formState.currentPasswordError)
            } else
                validState(binding.currentPasswordInputText, R.drawable.ic_check)

            if (formState.newPasswordError != null) {
                binding.newPasswordInputText.error =
                    getString(formState.newPasswordError)
            } else
                validState(binding.newPasswordInputText, R.drawable.ic_check)

            if (formState.confirmPasswordError != null) {
                binding.confirmPasswordInputText.error =
                    getString(formState.confirmPasswordError)
            } else
                validState(binding.confirmPasswordInputText, R.drawable.ic_check)

            if (formState.isDataValid) {
                changePasswordViewModel.changePassword(loggedInUser.id)
            }
        })


        //Waiting for Api response
        changePasswordViewModel.changePasswordResult.observe(this@ChangePasswordActivity) {
            val apiResult = it
            if (apiResult?.success == true) {
                showAlertDialog(
                    this, Constants.SUCCESS,
                    getString(R.string.password_changed_successfully),
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            onBackPressed()

                        }

                    })
            } else {
                showErrorDialog(
                    this,
                    apiResult?.message
                )
            }
        }
    }
}
