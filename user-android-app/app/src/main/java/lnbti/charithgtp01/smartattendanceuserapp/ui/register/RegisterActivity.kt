package lnbti.charithgtp01.smartattendanceuserapp.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SUCCESS
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityRegisterBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.model.Company
import lnbti.charithgtp01.smartattendanceuserapp.ui.login.LoginActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showConfirmAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBarWithoutHomeButton
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.normalState
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.setErrorBgToSelectLayout
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.setNormalBgToSelectLayout
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateWithoutHistory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    private var dialog: DialogFragment? = null
    private val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        viewModelObservers()
    }

    private fun initiateDataBinding() {
        //Data binding
        ViewModelProvider(this)[RegisterViewModel::class.java].apply {
            registerViewModel = this
            DataBindingUtil.setContentView<ActivityRegisterBinding?>(
                this@RegisterActivity,
                R.layout.activity_register
            ).apply {
                binding = this
                vm = registerViewModel
                lifecycleOwner = this@RegisterActivity

                Gson().apply {
                    fromJson(
                        intent.getStringExtra(Constants.OBJECT_STRING),
                        Company::class.java
                    ).apply {
                        registerViewModel.setCompany(this)
                    }
                }


                etEmployeeID.isEnabled = false

                initiateActionBarWithoutHomeButton(
                    actionBar.mainLayout,
                    getString(R.string.user_registration)
                ) { onBackPressed() }

                btnSubmit.setOnClickListener {
                    validateFields()
                }

                etDOB.setOnClickListener {
                    DatePickerDialog(
                        this@RegisterActivity, dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }

                // Observe data from the inner ViewModel (RadioGroup) using the outer ViewModel
                selectGenderLayout.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                    run {
                        findViewById<RadioButton?>(checkedId).apply {
                            text.toString().apply {
                                setSelectedRadioButtonValue(this)
                            }
                        }
                    }
                }

                setFocusChangeListener(etFirstName)
                setFocusChangeListener(etLastName)
                setFocusChangeListener(etNIC)
                setFocusChangeListener(etEmail)
                setFocusChangeListener(etContact)
                setFocusChangeListener(etUserName)
                setFocusChangeListener(etPassword)
                setFocusChangeListener(etConfirmPassword)
            }


        }

    }

    /**
     * Date Picker Dialog Listener
     */
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat(getString(R.string.date_format), Locale.ENGLISH)
            binding.etDOB.setText(sdf.format(cal.time))

        }

    private fun viewModelObservers() {

        registerViewModel.apply {
            /* Show error message in the custom error dialog */
            errorMessage.observe(this@RegisterActivity) {
                showErrorDialog(
                    this@RegisterActivity,
                    it
                )
            }

            isDialogVisible.observe(this@RegisterActivity) {
                when {
                    it -> dialog = showProgressDialog(
                        this@RegisterActivity,
                        getString(R.string.wait)
                    )

                    else -> dialog?.dismiss()
                }
                /* Show dialog when calling the API */
                /* Dismiss dialog after updating the data list to recycle view */
            }

            //If all fields are correct call the change password api
            registerForm.observe(this@RegisterActivity, Observer { state ->
                val formState = state ?: return@Observer
                formState.apply {
                    binding.apply {
                        firstNameError?.let {
                            firstNameInputText.error = getString(it)
                        } ?: normalState(firstNameInputText)

                        lastNameError?.let {
                            lastNameInputText.error = getString(it)
                        } ?: normalState(lastNameInputText)


                        nicError?.let {
                            nicInputText.error = getString(it)
                        } ?: normalState(nicInputText)

                        dobError?.let {
                            dobInputText.error = getString(it)
                        } ?: normalState(dobInputText)

                        genderError?.let {
                            setErrorBgToSelectLayout(
                                this@RegisterActivity,
                                selectGenderLayout,
                                getString(it)
                            )
                        } ?: setNormalBgToSelectLayout(
                            this@RegisterActivity,
                            selectGenderLayout
                        )

                        contactError?.let {
                            contactInputText.error =
                                getString(it)
                        } ?: normalState(contactInputText)

                        emailError?.let {
                            emailInputText.error =
                                getString(it)
                        } ?: normalState(emailInputText)

                        userNameError?.let {
                            usernameInputText.error =
                                getString(it)
                        } ?: normalState(usernameInputText)

                        newPasswordError?.let {
                            passwordInputText.error =
                                getString(it)
                        } ?: normalState(passwordInputText)

                        confirmPasswordError?.let {
                            confirmPasswordInputText.error =
                                getString(it)
                        } ?: normalState(confirmPasswordInputText)



                        when {
                            isDataValid -> showConfirmAlertDialog(
                                this@RegisterActivity,
                                getString(R.string.confirm_registration),
                                object : ConfirmDialogButtonClickListener {
                                    override fun onPositiveButtonClick() {
                                        when {
                                            NetworkUtils.isNetworkAvailable() -> register()
                                            else -> registerViewModel.setErrorMessage(getString(R.string.no_internet))
                                        }
                                    }

                                    override fun onNegativeButtonClick() {

                                    }
                                })
                        }
                    }
                }
            })


            //Waiting for Success response
            isSuccess.observe(this@RegisterActivity) {
                showAlertDialog(
                    this@RegisterActivity, SUCCESS,
                    ResourceConstants.USER_REGISTERED_SUCCESS,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            navigateWithoutHistory(
                                this@RegisterActivity,
                                LoginActivity::class.java
                            )

                        }

                    })
            }
        }


    }
}

