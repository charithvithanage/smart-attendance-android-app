package lnbti.charithgtp01.smartattendanceadminapp.ui.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityLoginBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.InputTextListener
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.inputTextInitiateMethod
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.validState

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nic = binding.nic
        val nicInputText = binding.nicInputText
        val password = binding.etPassword
        val passwordInputText = binding.passwordInputText
        val login = binding.login
        val loading = binding.loading


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        //UI initiation
        inputTextInitiateMethod(nicInputText,nic, object : InputTextListener {
            override fun validateUI() {
                loginViewModel.loginDataChanged(
                    nic?.text.toString(),
                    password?.text.toString()
                )
            }
        })

        inputTextInitiateMethod(passwordInputText,password, object : InputTextListener {
            override fun validateUI() {
                loginViewModel.loginDataChanged(
                    nic?.text.toString(),
                    password?.text.toString()
                )
            }
        })

        login.setOnClickListener {
            loginViewModel.loginDataChanged(
                nic?.text.toString(),
                password?.text.toString()
            )
        }

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            if (loginState.nicError != null) {
                nicInputText?.error = getString(loginState.nicError)
            } else
                validState(nicInputText, R.drawable.ic_check)


            if (loginState.passwordError != null) {
                passwordInputText?.error = getString(loginState.passwordError)
            }

            if (loginState.isDataValid) {
                loading.visibility = View.VISIBLE
                loginViewModel.login(nic?.text.toString(), password?.text.toString())
            }
        })


        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun TextInputEditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}