package lnbti.charithgtp01.smartattendanceadminapp.ui.login

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.MainActivity
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.ACCESS_TOKEN
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.LOGGED_IN_USER
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityLoginBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.InputTextListener
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.inputTextInitiateMethod
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.navigateToAnotherActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.saveObjectInSharedPref

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private var dialog: DialogFragment? = null
    private lateinit var username: TextInputEditText
    private lateinit var usernameInputText: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var passwordInputText: TextInputLayout
    private lateinit var login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loggedInUser = getObjectFromSharedPref(this@LoginActivity, LOGGED_IN_USER)
        //Already user is logged in navigate to home page
        if (loggedInUser != null) {
            navigateToAnotherActivity(
                this@LoginActivity,
                MainActivity::class.java
            )
        } else {
            initiateDataBinding()
            initiateView()
            viewModelObservers()
        }
    }

    private fun initiateDataBinding() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        //Data binding
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.vm = loginViewModel
        binding.lifecycleOwner = this
    }

    private fun initiateView() {

        username = binding.username
        usernameInputText = binding.usernameInputText
        password = binding.etPassword
        passwordInputText = binding.passwordInputText
        login = binding.login

//        username.setText("Charith")
//        password.setText("Charith@1234")

        login.setOnClickListener {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password?.text.toString()
            )
        }

        binding.vm?.setFocusChangeListener(binding.etPassword, binding.passwordInputText)
    }

    private fun viewModelObservers() {

        /* Show error message in the custom error dialog */
        loginViewModel.errorMessage.observe(this@LoginActivity) {
            showErrorDialog(
                this@LoginActivity,
                it
            )
        }

        loginViewModel.isDialogVisible.observe(this@LoginActivity) {
            if (it) {
                /* Show dialog when calling the API */
                dialog = showProgressDialog(
                    this@LoginActivity,
                    getString(R.string.wait)
                )
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            if (loginState.nicError != null) {
                usernameInputText?.error = getString(loginState.nicError)
            } else
                validState(usernameInputText, R.drawable.ic_check)


            if (loginState.passwordError != null) {
                passwordInputText?.error = getString(loginState.passwordError)
            }
            /* Need to enter above data to successful login
               "email": "eve.holt@reqres.in",
               "password": "cityslicka"
            */
            if (loginState.isDataValid) {
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        })


        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.success) {
                saveObjectInSharedPref(
                    this,
                    LOGGED_IN_USER,
                    loginResult.data.toString()
                ) { navigateToAnotherActivity(this, MainActivity::class.java) }
            } else {
                DialogUtils.showErrorDialog(this, loginResult.message)
            }

        })
    }
}