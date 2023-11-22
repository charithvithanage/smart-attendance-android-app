package lnbti.charithgtp01.smartattendanceadminapp.ui.othersettings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants.BIO_METRIC_ENABLE_STATUS
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityOtherSettingsBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.goToHomeActivity

@AndroidEntryPoint
class OtherSettingsActivity : AppCompatActivity() {
    private var binding: ActivityOtherSettingsBinding? = null
    private lateinit var viewModel: OtherSettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        viewModelObservers()

    }

    private fun initView() {
        initiateActionBar(
            binding?.actionBar?.mainLayout!!,
            ResourceConstants.OTHER_SETTINGS,
            object : ActionBarListener {
                override fun backPressed() {
                    onBackPressed()
                }

                override fun homePressed() {
                    goToHomeActivity(this@OtherSettingsActivity)
                }
            })

        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )


        // Set an OnCheckedChangeListener to update the LiveData
        binding?.switchFinger?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setBiometricEnabled(isChecked)
        }

        viewModel.setBiometricEnabled(sharedPref.getBoolean(BIO_METRIC_ENABLE_STATUS, false))


    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_settings)
        viewModel = ViewModelProvider(this)[OtherSettingsViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
    }

    private fun viewModelObservers() {
        viewModel.getBiometricEnabledLiveData().observe(this) { enabled ->
            binding?.switchFinger?.isChecked = enabled
            val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            val editor = sharedPref.edit()
            editor.putBoolean(BIO_METRIC_ENABLE_STATUS, enabled)
            editor.apply()
        }
    }
}