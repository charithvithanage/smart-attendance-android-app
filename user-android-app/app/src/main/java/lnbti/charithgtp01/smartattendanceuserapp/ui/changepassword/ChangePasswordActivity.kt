package lnbti.charithgtp01.smartattendanceuserapp.ui.changepassword

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
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.LOGGED_IN_USER
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.CHANGE_PASSWORD
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityChangePasswordBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils.Companion.isNetworkAvailable
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils

/**
 * `ChangePasswordActivity` is responsible for handling the UI and user interactions related
 * to changing the password. It utilizes data binding and a corresponding ViewModel
 * (`ChangePasswordViewModel`) to manage the business logic.
 *
 * @property changePasswordViewModel The ViewModel responsible for handling password change logic.
 * @property binding Data binding instance for connecting UI elements with the ViewModel.
 * @property dialog DialogFragment for showing progress or error dialogs.
 * @property loggedInUser The currently logged-in user.
 */
@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private lateinit var binding: ActivityChangePasswordBinding
    private var dialog: DialogFragment? = null
    private lateinit var loggedInUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        viewModelObservers()
    }

    /**
     * Initializes data binding, sets up the ViewModel, and configures UI elements.
     */
    private fun initiateDataBinding() {
        ViewModelProvider(this)[ChangePasswordViewModel::class.java].apply {
            changePasswordViewModel = this
            binding =
                DataBindingUtil.setContentView<ActivityChangePasswordBinding?>(
                    this@ChangePasswordActivity,
                    R.layout.activity_change_password
                ).apply {
                    //Data binding
                    vm = changePasswordViewModel
                    lifecycleOwner = this@ChangePasswordActivity

                    initiateActionBar(
                        actionBar?.mainLayout!!,
                        CHANGE_PASSWORD,
                        object : ActionBarListener {
                            override fun backPressed() {
                                onBackPressed()
                            }

                            override fun homePressed() {
                                Utils.goToHomeActivity(this@ChangePasswordActivity)
                            }
                        })

                    btnSubmit.setOnClickListener {
                        changePasswordViewModel.validateFields()
                    }
                }

            Gson().apply {
                val loggedInUserString =
                    Utils.getObjectFromSharedPref(this@ChangePasswordActivity, LOGGED_IN_USER)
                loggedInUser = fromJson(loggedInUserString, User::class.java)
            }
        }
    }

    /**
     * Sets up observers for ViewModel LiveData objects to handle UI updates.
     */
    private fun viewModelObservers() {

        changePasswordViewModel.apply {
            // If all fields are correct call the change password api
            changePasswordForm.observe(this@ChangePasswordActivity, Observer { state ->

                val formState = state ?: return@Observer

                formState.apply {
                    binding.apply {
                        currentPasswordError?.let {
                            currentPasswordInputText.error = getString(it)
                        } ?: validState(currentPasswordInputText, R.drawable.ic_check)

                        newPasswordError?.let {
                            newPasswordInputText.error = getString(it)
                        } ?: validState(newPasswordInputText, R.drawable.ic_check)

                        confirmPasswordError?.let {
                            confirmPasswordInputText.error = getString(it)
                        } ?: validState(confirmPasswordInputText, R.drawable.ic_check)

                        when {
                            isDataValid -> when {
                                isNetworkAvailable() ->
                                    changePassword(loggedInUser.nic)

                                else -> showErrorDialog(
                                    this@ChangePasswordActivity,
                                    getString(R.string.no_internet)
                                )
                            }
                        }
                    }
                }
            })


            //Waiting for Api response
            changePasswordResult.observe(this@ChangePasswordActivity) {
                it?.let {
                    dialog?.dismiss()

                    when (it.success) {
                        true -> showAlertDialog(
                            this@ChangePasswordActivity, Constants.SUCCESS,
                            getString(R.string.password_changed_successfully),
                            object : CustomAlertDialogListener {
                                override fun onDialogButtonClicked() {
                                    onBackPressed()

                                }

                            })

                        else -> showErrorDialog(
                            this@ChangePasswordActivity,
                            it.message
                        )
                    }
                }
            }

            isDialogVisible.observe(this@ChangePasswordActivity) {
                when {
                    it -> dialog = showProgressDialog(
                        this@ChangePasswordActivity,
                        getString(R.string.wait)
                    )

                    else -> dialog?.dismiss()
                }
                /* Show dialog when calling the API */
                /* Dismiss dialog after updating the data list to recycle view */
            }
        }
    }
}
