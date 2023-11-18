package lnbti.charithgtp01.smartattendanceuserapp.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.CHANGE_PASSWORD
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.GET_DEVICE_ID
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.OTHER_SETTINGS
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivitySettingsBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.model.SettingsObject
import lnbti.charithgtp01.smartattendanceuserapp.ui.changepassword.ChangePasswordActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.othersettings.OtherSettingsActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.qr.device.DeviceIDQRActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivity

/**
 * Settings Page
 */
@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsListViewModel
    private lateinit var settingsAdapterListAdapter: SettingsAdapterListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initiateView()
        initiateAdapter()
        viewModelObservers()

    }

    private fun initiateView() {
        UIUtils.initiateActionBar(
            binding.actionBar.mainLayout,
            getString(R.string.action_settings),
            object : ActionBarListener {
                override fun backPressed() {
                    onBackPressed()
                }

                override fun homePressed() {
                    Utils.goToHomeActivity(this@SettingsActivity)
                }
            })


    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView<ActivitySettingsBinding?>(this, R.layout.activity_settings).apply {
            viewModel = ViewModelProvider(this@SettingsActivity)[SettingsListViewModel::class.java]
            vm = viewModel
            lifecycleOwner = this@SettingsActivity
        }

    }

    /**
     * Live Data Updates
     */
    private fun viewModelObservers() {
        /* Observer to catch list data
      * Update Recycle View Items using Diff Utils
      */
        viewModel.settingsList.observe(this@SettingsActivity) {
            settingsAdapterListAdapter.submitList(it)
        }
    }

    /**
     * Recycle View data configuration
     */
    private fun initiateAdapter() {
        /* Initiate Adapter */
        settingsAdapterListAdapter =
            SettingsAdapterListAdapter(object : SettingsAdapterListAdapter.OnItemClickListener {
                override fun itemClick(item: SettingsObject) {
                    when (item.name) {
                        GET_DEVICE_ID -> navigateToAnotherActivity(
                            this@SettingsActivity,
                            DeviceIDQRActivity::class.java
                        )

                        OTHER_SETTINGS -> {
                            navigateToAnotherActivity(
                                this@SettingsActivity,
                                OtherSettingsActivity::class.java
                            )
                        }

                        CHANGE_PASSWORD -> {
                            navigateToAnotherActivity(
                                this@SettingsActivity,
                                ChangePasswordActivity::class.java
                            )
                        }
                    }
                }
            })

        /* Set Adapter to Recycle View */
        binding.recyclerView.also { it2 ->
            it2.adapter = settingsAdapterListAdapter
        }
    }

}