package lnbti.charithgtp01.smartattendanceuserapp.ui.qr.attendance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityAttendanceQrBinding
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityDeviceIdQrBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarWithoutHomeListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBarWithoutHomeButton

/**
 * Business User Attendance Handshake QR Page
 */
@AndroidEntryPoint
class AttendanceQRActivity : AppCompatActivity() {
    private var binding: ActivityAttendanceQrBinding? = null
    private lateinit var viewModel: AttendanceQRViewModel

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
            getString(R.string.attendance_qr),
            ActionBarWithoutHomeListener { onBackPressed() })
    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_attendance_qr)
        viewModel = ViewModelProvider(this)[AttendanceQRViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
    }
}