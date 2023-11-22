package lnbti.charithgtp01.smartattendanceadminapp.ui.qr

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityDeviceIdQrBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.goToHomeActivity

@AndroidEntryPoint
class DeviceIDQRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceIdQrBinding
    private lateinit var viewModel: DeviceIDQRViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        viewModelObservers()
    }

    /**
     * Live Data Updates
     */
    private fun viewModelObservers() {
        // Observe the LiveData to receive the generated QR code data
        viewModel.generatedQRCodeData.observe(this) { qrCodeBitmap ->
            // Use the generated QR code Bitmap here (e.g., display it in an ImageView)
            binding.qrCodeView.setImageBitmap(qrCodeBitmap)
        }

        /* Show error message in the custom error dialog */
        viewModel.errorMessage.observe(this@DeviceIDQRActivity) {
            showAlertDialog(
                this@DeviceIDQRActivity,
                Constants.FAIL,
                it,
                object : CustomAlertDialogListener {
                    override fun onDialogButtonClicked() {
                        finish()
                    }
                })
        }
    }

    private fun initView() {
        initiateActionBar(
            binding.actionBar.mainLayout,
            getString(R.string.employee_qr),
            object : ActionBarListener {
                override fun backPressed() {
                    onBackPressed()
                }

                override fun homePressed() {
                    goToHomeActivity(this@DeviceIDQRActivity)
                }
            })

        val gson = Gson()
        val objectString = intent.getStringExtra(Constants.OBJECT_STRING)
        val user = gson.fromJson(
            objectString,
            lnbti.charithgtp01.smartattendanceadminapp.model.User::class.java
        )

        viewModel.setData(user)
    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_id_qr)
        viewModel = ViewModelProvider(this)[DeviceIDQRViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }
}