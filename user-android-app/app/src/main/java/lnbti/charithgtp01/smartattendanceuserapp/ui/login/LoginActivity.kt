package lnbti.charithgtp01.smartattendanceuserapp.ui.login

import android.app.Dialog
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.MainActivity
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.ACCESS_TOKEN
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityLoginBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.InputTextListener
import lnbti.charithgtp01.smartattendanceuserapp.ui.qr.DeviceIDQRActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.qr.DeviceIDQRActivity_GeneratedInjector
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.inputTextInitiateMethod
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.validState
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getAndroidId
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.saveObjectInSharedPref
import java.util.concurrent.atomic.AtomicInteger

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private var dialog: Dialog? = null
    private lateinit var username: TextInputEditText
    private lateinit var usernameInputText: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var passwordInputText: TextInputLayout
    private lateinit var login: Button

    //This counter is using for catch the app logo click event counter
    private var atomicInteger = AtomicInteger(0)
    private var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initiateDataBinding()
        initiateView()
        initiateProgressDialog()
        viewModelObservers()
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

        username.setText("eve.holt@reqres.in")
        password.setText("cityslicka")

        //UI initiation
        inputTextInitiateMethod(usernameInputText, username, object : InputTextListener {
            override fun validateUI() {
                loginViewModel.loginDataChanged(
                    username?.text.toString(),
                    password?.text.toString()
                )
            }
        })

        inputTextInitiateMethod(passwordInputText, password, object : InputTextListener {
            override fun validateUI() {
                loginViewModel.loginDataChanged(
                    username?.text.toString(),
                    password?.text.toString()
                )
            }
        })

        login.setOnClickListener {
            loginViewModel.loginDataChanged(
                username?.text.toString(),
                password?.text.toString()
            )
        }

        binding.appLogo.setOnClickListener {
            i = atomicInteger.getAndIncrement();

            //After 4 click on the logo navigate to Device ID qr activity
            if (i >= 4) {
                atomicInteger = AtomicInteger(0)
                i = 0
                val androidID = getAndroidId(this)
                goToQRActivity(androidID)
            }
        }
    }

    private fun viewModelObservers() {
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
                dialog?.show()
                loginViewModel.login(username?.text.toString(), password?.text.toString())
            }
        })


        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            dialog?.dismiss()

            if (loginResult.token != null) {
                saveObjectInSharedPref(
                    this,
                    ACCESS_TOKEN,
                    loginResult.token
                ) { navigateToAnotherActivity(this, MainActivity::class.java) }
            } else if (loginResult.error != null) {
                showErrorDialog(this, loginResult.error, object : DialogButtonClickListener {
                    override fun onButtonClick() {

                    }
                })

            }

        })
    }

    /**
     * Progress Dialog Initiation
     */
    private fun initiateProgressDialog() {
        dialog = showProgressDialog(this, getString(R.string.wait))
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

