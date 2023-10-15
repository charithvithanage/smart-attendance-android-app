package lnbti.charithgtp01.smartattendanceuserapp.ui.qr.attendance

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.Keystore
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityAttendanceQrBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarWithoutHomeListener
import lnbti.charithgtp01.smartattendanceuserapp.model.User
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
        setData()
        viewModelObservers()
    }

    private fun setData() {
        val encryptedString = intent.getStringExtra(Constants.OBJECT_STRING) as String
        Log.d(Constants.TAG,encryptedString)
        try {
            val gson = Gson()
            val selectedUserString = Keystore.decrypt(Constants.SECURE_KEY,encryptedString)
            var selectedUser = gson.fromJson(selectedUserString, User::class.java)
            viewModel.generateQRCode(encryptedString, selectedUser)
        } catch (e: Exception) {
            e.printStackTrace()
        }


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

//    fun encryptString(value: String): String? {
//        var encryptedCredential: String? = null
//        try {
//            val compressed: ByteArray = compress(value)!!
//            val decoded = String(compressed, StandardCharsets.ISO_8859_1)
//            val secureKey: String = Constants.SECURE_KEY
//            try {
//                encryptedCredential = Keystore.encrypt(decoded, secureKey)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            Log.d(Constants.TAG, "Compressed string $encryptedCredential")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        return encryptedCredential
//    }

}