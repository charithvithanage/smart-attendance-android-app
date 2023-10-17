package lnbti.charithgtp01.smartattendanceadminapp.ui.login

import android.app.Dialog
import android.content.Context
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
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.BiometricAuthenticationHelper
import lnbti.charithgtp01.smartattendanceadminapp.Keystore.Companion.decrypt
import lnbti.charithgtp01.smartattendanceadminapp.Keystore.Companion.encrypt
import lnbti.charithgtp01.smartattendanceadminapp.MainActivity
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.ACCESS_TOKEN
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.LOGGED_IN_USER
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.SECURE_KEY
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityLoginBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.InputTextListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.SuccessListener
import lnbti.charithgtp01.smartattendanceadminapp.model.Credential
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.inputTextInitiateMethod
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.navigateToAnotherActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.saveMultipleObjectsInSharedPref
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
    val gson = Gson()

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

        binding.bioMetricAuthentication.setOnClickListener {
            val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )

            val bioMetricAuthenticationEnableStatus =
                sharedPref.getBoolean(ResourceConstants.BIO_METRIC_ENABLE_STATUS, false)
            val lastSignInObject =
                getObjectFromSharedPref(
                    this@LoginActivity,
                    ResourceConstants.LAST_LOGGED_IN_CREDENTIAL
                )
            if (bioMetricAuthenticationEnableStatus && lastSignInObject != null) {
                val biometricHelper =
                    BiometricAuthenticationHelper(this) // 'this' should be a valid context
                biometricHelper.authenticateBiometric(
                    "Biometric Authentication",
                    "Verify your identity",
                    "Place your finger on the sensor",
                    {
                        // Biometric authentication successful
                        // You can perform your actions here


                        var decryptedCredential: String? = null
                        try {
                            decryptedCredential = decrypt(SECURE_KEY, lastSignInObject)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val credential = gson.fromJson(decryptedCredential, Credential::class.java)

                         loginViewModel.login(
                            credential.username,
                            credential.password
                        )

                    },
                    { error ->
                        // Handle authentication error, e.g., show an error message
                        showErrorDialog(this@LoginActivity, "Login Error")
                    }
                )
            } else {
                showErrorDialog(this@LoginActivity, getString(R.string.bio_metric_disabled))
            }
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
                val hashMap = HashMap<String, String>()

                // Add values to the HashMap
                hashMap[LOGGED_IN_USER] = loginResult.data.toString()
                try {
                    val credential = Credential(
                        username.text.toString(),
                        password.text.toString()
                    )

                    val encryptedCredential = encrypt(SECURE_KEY, gson.toJson(credential))
                    hashMap[ResourceConstants.LAST_LOGGED_IN_CREDENTIAL] =
                        encryptedCredential.toString()

                } catch (e: Exception) {

                }
                saveMultipleObjectsInSharedPref(this@LoginActivity, hashMap,
                    SuccessListener {
                        navigateToAnotherActivity(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                    })
            } else {
                showErrorDialog(this, loginResult.message)
            }

        })
    }
}