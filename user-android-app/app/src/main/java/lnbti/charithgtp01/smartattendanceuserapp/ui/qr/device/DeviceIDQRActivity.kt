package lnbti.charithgtp01.smartattendanceuserapp.ui.qr.device

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityDeviceIdQrBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarWithoutHomeListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBarWithoutHomeButton

@AndroidEntryPoint
class DeviceIDQRActivity : AppCompatActivity() {
    private var binding: ActivityDeviceIdQrBinding? = null
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
            binding?.qrCodeView?.setImageBitmap(qrCodeBitmap)
        }
    }

    private fun initView() {
        initiateActionBarWithoutHomeButton(
            binding?.actionBar?.mainLayout!!,
            getString(R.string.device_id),
            ActionBarWithoutHomeListener { onBackPressed() })
    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_id_qr)
        viewModel = ViewModelProvider(this)[DeviceIDQRViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
    }
}