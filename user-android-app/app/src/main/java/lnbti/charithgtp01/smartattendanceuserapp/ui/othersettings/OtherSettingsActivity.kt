package lnbti.charithgtp01.smartattendanceuserapp.ui.othersettings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.BIO_METRIC_ENABLE_STATUS
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.OTHER_SETTINGS
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityOtherSettingsBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.goToHomeActivity

/**
 * `OtherSettingsActivity` is an Android activity that allows users to manage various settings,
 * including biometric settings. It uses data binding to bind UI components with the associated
 * [OtherSettingsViewModel] and facilitates the interaction between the UI and the underlying
 * data layer.
 *
 * @constructor Creates an instance of [OtherSettingsActivity].
 */
@AndroidEntryPoint
class OtherSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtherSettingsBinding
    private lateinit var viewModel: OtherSettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        viewModelObservers()
    }

    /**
     * Initializes data binding by associating the layout file, setting up the action bar,
     * and establishing the [OtherSettingsViewModel] for the activity.
     */
    private fun initiateDataBinding() {
        ViewModelProvider(this)[OtherSettingsViewModel::class.java].apply {
            viewModel = this
            DataBindingUtil.setContentView<ActivityOtherSettingsBinding?>(
                this@OtherSettingsActivity,
                R.layout.activity_other_settings
            ).apply {
                binding = this
                vm = viewModel
                lifecycleOwner = this@OtherSettingsActivity
                initiateActionBar(
                    actionBar.mainLayout,
                    OTHER_SETTINGS,
                    object : ActionBarListener {
                        override fun backPressed() {
                            onBackPressed()
                        }

                        override fun homePressed() {
                            goToHomeActivity(this@OtherSettingsActivity)
                        }
                    })

                // Set an OnCheckedChangeListener to update the LiveData
                switchFinger.setOnCheckedChangeListener { _, isChecked ->
                    setBiometricEnabled(isChecked)
                }

            }

            getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            ).apply {
                setBiometricEnabled(getBoolean(BIO_METRIC_ENABLE_STATUS, false))
            }
        }
    }

    /**
     * Observes changes in the [OtherSettingsViewModel] and updates the UI accordingly.
     * Additionally, updates the shared preferences based on the biometric enable status.
     */
    private fun viewModelObservers() {

        viewModel.getBiometricEnabledLiveData().observe(this) { enabled ->
            binding.switchFinger.isChecked = enabled
            getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            ).apply {
                this.edit().run {
                    putBoolean(BIO_METRIC_ENABLE_STATUS, enabled)
                    apply()
                }
            }
        }
    }

}