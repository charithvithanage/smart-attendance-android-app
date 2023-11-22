package lnbti.charithgtp01.smartattendanceuserapp.ui.qr.attendance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.other.Keystore
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityAttendanceQrBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarWithoutHomeListener
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBarWithoutHomeButton

/**
 * Activity responsible for displaying the QR code for Business User Attendance Handshake purposes.
 *
 * This activity uses View Binding and Android ViewModel to manage UI components and data.
 *
 * @constructor Creates an [AttendanceQRActivity].
 */
@AndroidEntryPoint
class AttendanceQRActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendanceQrBinding
    private lateinit var viewModel: AttendanceQRViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        viewModelObservers()
    }

    /**
     * Initializes View Binding and sets up data binding for the activity.
     * Also initiates the generation of the QR code based on the intent data.
     */
    private fun initiateDataBinding() {
        ViewModelProvider(this)[AttendanceQRViewModel::class.java].apply {
            viewModel = this
            binding = DataBindingUtil.setContentView<ActivityAttendanceQrBinding?>(
                this@AttendanceQRActivity,
                R.layout.activity_attendance_qr
            ).apply {
                binding = this
                vm = viewModel
                lifecycleOwner = this@AttendanceQRActivity

                // Set up action bar without home button
                initiateActionBarWithoutHomeButton(
                    actionBar.mainLayout,
                    getString(R.string.attendance_qr),
                    ActionBarWithoutHomeListener { onBackPressed() })
            }

            // Extract data from intent, decrypt it, and generate QR code
            intent.getStringExtra(Constants.OBJECT_STRING)?.let {
                try {
                    Gson().apply {
                        generateQRCode(it, fromJson(Keystore.decrypt(Constants.SECURE_KEY, it), User::class.java))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Observes the ViewModel's LiveData to receive updates when the QR code is generated.
     */
    private fun viewModelObservers() {
        // Observe the LiveData to receive the generated QR code data
        viewModel.generatedQRCodeData.observe(this) { qrCodeBitmap ->
            // Use the generated QR code Bitmap here (e.g., display it in an ImageView)
            binding.qrCodeView.setImageBitmap(qrCodeBitmap)
        }
    }
}