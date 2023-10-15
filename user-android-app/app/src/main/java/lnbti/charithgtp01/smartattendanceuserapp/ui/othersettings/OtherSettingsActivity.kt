package lnbti.charithgtp01.smartattendanceuserapp.ui.othersettings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.ui.othersettings.OtherSettingsViewModel
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.goToHomeActivity
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.OTHER_SETTINGS
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityOtherSettingsBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener

@AndroidEntryPoint
class OtherSettingsActivity : AppCompatActivity() {
    private var binding: ActivityOtherSettingsBinding? = null
    private lateinit var viewModel: OtherSettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
    }

    private fun initView() {
        initiateActionBar(
            binding?.actionBar?.mainLayout!!,
            OTHER_SETTINGS,
            object : ActionBarListener {
                override fun backPressed() {
                    onBackPressed()
                }

                override fun homePressed() {
                    goToHomeActivity(this@OtherSettingsActivity)
                }
            })
    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_settings)
        viewModel = ViewModelProvider(this)[OtherSettingsViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
    }
}