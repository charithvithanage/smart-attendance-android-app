package lnbti.charithgtp01.smartattendanceuserapp.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivitySettingsBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.model.SettingsObject
import lnbti.charithgtp01.smartattendanceuserapp.ui.changepassword.ChangePasswordActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.othersettings.OtherSettingsActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.qr.device.DeviceIDQRActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils

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
            binding?.actionBar?.mainLayout!!,
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        viewModel = ViewModelProvider(this)[SettingsListViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = this@SettingsActivity
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
                        getString(R.string.get_device_id) -> Utils.navigateToAnotherActivity(
                            this@SettingsActivity,
                            DeviceIDQRActivity::class.java
                        )

                        getString(R.string.other_settings) -> {
                            Utils.navigateToAnotherActivity(
                                this@SettingsActivity,
                                OtherSettingsActivity::class.java
                            )
                        }

                        getString(R.string.change_password) -> {
                            Utils.navigateToAnotherActivity(
                                this@SettingsActivity,
                                ChangePasswordActivity::class.java
                            )
                        }
                    }
                }
            })

        /* Set Adapter to Recycle View */
        binding?.recyclerView.also { it2 ->
            it2?.adapter = settingsAdapterListAdapter
        }
    }

}