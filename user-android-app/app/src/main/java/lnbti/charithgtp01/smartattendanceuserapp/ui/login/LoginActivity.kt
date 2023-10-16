package lnbti.charithgtp01.smartattendanceuserapp.ui.login

import android.content.Context
import android.os.Bundle
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
import lnbti.charithgtp01.smartattendanceuserapp.BiometricAuthenticationHelper
import lnbti.charithgtp01.smartattendanceuserapp.Keystore.Companion.decrypt
import lnbti.charithgtp01.smartattendanceuserapp.Keystore.Companion.encrypt
import lnbti.charithgtp01.smartattendanceuserapp.MainActivity
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.LOGGED_IN_USER
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.USER_ROLE
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityLoginBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.SuccessListener
import lnbti.charithgtp01.smartattendanceuserapp.model.Credential
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.qr.device.DeviceIDQRActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.searchcompany.SearchCompanyActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.changeUiSize
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getAndroidId
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.saveMultipleObjectsInSharedPref
import java.util.concurrent.atomic.AtomicInteger

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

    //This counter is using for catch the app logo click event counter
    private var atomicInteger = AtomicInteger(0)
    private var i = 0
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
        changeUiSize(this, binding.appLogo, 1, 1, 30)
        username = binding.username
        usernameInputText = binding.usernameInputText
        password = binding.etPassword
        passwordInputText = binding.passwordInputText
        login = binding.login

        login.setOnClickListener {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
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
                            decryptedCredential = decrypt(lastSignInObject)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val credential = gson.fromJson(decryptedCredential, Credential::class.java)

                        dialog = showProgressDialog(this, getString(R.string.wait))
                        val deviceID = getAndroidId(this@LoginActivity)
                        loginViewModel.login(
                            deviceID,
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

        //After 4 click on the logo navigate to Device ID qr activity
        binding.appLogo.setOnClickListener {
            i = atomicInteger.getAndIncrement()
            if (i >= 4) {
                atomicInteger = AtomicInteger(0)
                i = 0
                val androidID = getAndroidId(this)
                goToQRActivity(androidID)
            }
        }

        binding.btnSignUp.setOnClickListener {
            navigateToAnotherActivity(
                this@LoginActivity,
                SearchCompanyActivity::class.java
            )
        }
    }

    private fun viewModelObservers() {
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            if (loginState.nicError != null) {
                usernameInputText.error = getString(loginState.nicError)
            } else
                validState(usernameInputText, R.drawable.ic_check)


            if (loginState.passwordError != null) {
                passwordInputText.error = getString(loginState.passwordError)
            }
            /* Need to enter above data to successful login
               "email": "eve.holt@reqres.in",
               "password": "cityslicka"
            */
            if (loginState.isDataValid) {
                dialog = showProgressDialog(this, getString(R.string.wait))
                val deviceID = getAndroidId(this@LoginActivity)
                loginViewModel.login(
                    deviceID,
                    username.text.toString(),
                    password.text.toString()
                )
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            dialog?.dismiss()

            if (loginResult.success) {
                val loggedInUser = gson.fromJson(loginResult.data.toString(), User::class.java)
                val hashMap = HashMap<String, String>()

                // Add values to the HashMap
                hashMap[LOGGED_IN_USER] = loginResult.data.toString()
                hashMap[USER_ROLE] = loggedInUser.userRole

                try {
                    val credential=Credential(username.text.toString(),
                        password.text.toString())

//                    val encryptedCredential= encrypt(gson.toJson(credential))

                }catch (e:Exception){

                }

                if (loggedInUser.userType == ResourceConstants.ANDROID_USER) {
                    saveMultipleObjectsInSharedPref(this@LoginActivity, hashMap,
                        SuccessListener {
                            navigateToAnotherActivity(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                        })
                } else {
                    showErrorDialog(this, getString(R.string.no_permission_to_use))
                }

            } else {
                showErrorDialog(this, loginResult.message)
            }

        })
    }

    /**
     * Navigate to QR activity with generated Device ID
     */
    private fun goToQRActivity(data: String) {
        val navigationPathMap = HashMap<String, String>()
        navigationPathMap[OBJECT_STRING] = data
        navigateToAnotherActivityWithExtras(
            this@LoginActivity,
            DeviceIDQRActivity::class.java, navigationPathMap
        )
    }
}

