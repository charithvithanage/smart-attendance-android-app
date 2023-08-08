package lnbti.charithgtp01.smartattendanceuserapp.ui.register

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityRegisterBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBarWithoutHomeButton
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.normalState
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.setErrorBgToSelectLayout
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.setNormalBgToSelectLayout
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
        initiateView()
        viewModelObservers()
    }

    private fun initiateDataBinding() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)

        //Data binding
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.vm = registerViewModel
        binding.lifecycleOwner = this
    }

    private fun initiateView() {
        initiateActionBarWithoutHomeButton(
            binding.actionBar.mainLayout,
            getString(R.string.user_registration)
        ) { onBackPressed() }

        binding.btnSubmit.setOnClickListener {
            registerViewModel.validateFields()
        }

        binding.etDOB.setOnClickListener {
            DatePickerDialog(
                this@RegisterActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Observe data from the inner ViewModel (RadioGroup) using the outer ViewModel
        binding.selectGenderLayout.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            run {
                val radioButton: RadioButton = findViewById(checkedId)
                val selectedValue = radioButton.text.toString()
                registerViewModel.setSelectedRadioButtonValue(selectedValue)
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
        //If all fields are correct call the change password api
        registerViewModel.registerForm.observe(this@RegisterActivity, Observer {
            val formState = it ?: return@Observer

            if (formState.firstNameError != null) {
                binding.firstNameInputText.error =
                    getString(formState.firstNameError!!)
            } else
                normalState(binding.firstNameInputText)

            if (formState.lastNameError != null) {
                binding.lastNameInputText.error =
                    getString(formState.lastNameError!!)
            } else
                normalState(binding.lastNameInputText)

            if (formState.nicError != null) {
                binding.nicInputText.error =
                    getString(formState.nicError!!)
            } else
                normalState(binding.nicInputText)

            if (formState.employeeIDError != null) {
                binding.employeeIDInputText.error =
                    getString(formState.employeeIDError!!)
            } else
                normalState(binding.employeeIDInputText)

            if (formState.dobError != null) {
                binding.dobInputText.error =
                    getString(formState.dobError!!)
            } else
                normalState(binding.dobInputText)

            if (formState.genderError != null) {
                setErrorBgToSelectLayout(
                    this@RegisterActivity,
                    binding.selectGenderLayout,
                    getString(formState.genderError!!)
                )
            } else {
                setNormalBgToSelectLayout(this, binding.selectGenderLayout)
            }

            if (formState.contactError != null) {
                binding.contactInputText.error =
                    getString(formState.contactError!!)
            } else
                normalState(binding.contactInputText)

            if (formState.emailError != null) {
                binding.emailInputText.error =
                    getString(formState.emailError!!)
            } else
                normalState(binding.emailInputText)

            if (formState.userNameError != null) {
                binding.usernameInputText.error =
                    getString(formState.userNameError!!)
            } else
                normalState(binding.usernameInputText)

            if (formState.newPasswordError != null) {
                binding.passwordInputText.error =
                    getString(formState.newPasswordError!!)
            } else
                normalState(binding.passwordInputText)

            if (formState.confirmPasswordError != null) {
                binding.confirmPasswordInputText.error =
                    getString(formState.confirmPasswordError!!)
            } else
                normalState(binding.confirmPasswordInputText)

            if (formState.isDataValid) {
                dialog = showProgressDialog(this, getString(R.string.wait))
                registerViewModel.register()
            }
        })


        //Waiting for Api response
        registerViewModel.registerResult.observe(this@RegisterActivity) {
            val apiResult = it

            dialog?.dismiss()

            if (apiResult?.success == true) {
                showAlertDialog(
                    this, Constants.SUCCESS,
                    getString(R.string.user_registered_successfully),
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            onBackPressed()

                        }

                    })
            } else if (apiResult?.data != null) {
                showErrorDialog(this, apiResult.data)

            }

        }
    }
}

