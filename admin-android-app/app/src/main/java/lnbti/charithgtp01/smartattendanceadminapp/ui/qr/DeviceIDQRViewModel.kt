package lnbti.charithgtp01.smartattendanceadminapp.ui.qr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceadminapp.Keystore
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.SECURE_KEY
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants.ENCRYPTION_ERROR
import java.security.KeyStore
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class DeviceIDQRViewModel @Inject constructor(private val context: Context) : ViewModel() {

    private val _generatedQRCodeData = MutableLiveData<Bitmap>()
    val generatedQRCodeData: LiveData<Bitmap>
        get() = _generatedQRCodeData

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private fun generateQRCode(text: String) {
        // Implement your QR code generation logic here and set the result in _generatedQRCodeData
        val generatedQRCode = generateQRCodeBitmap(text)
        _generatedQRCodeData.value = generatedQRCode
    }

    /**
     * Method to convert String to Bitmap
     * @param text qr value
     */
    private fun generateQRCodeBitmap(text: String): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }

            return bitmap
        } catch (e: WriterException) {
            // Handle the exception if QR code generation fails
            e.printStackTrace()
            return Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
        }
    }

    fun setData(user: lnbti.charithgtp01.smartattendanceadminapp.model.User) {
        _name.value = user.firstName + " " + user.lastName
        try {
            val encryptedValue = Keystore.encrypt(SECURE_KEY, user.nic)
            if (encryptedValue != null)
                generateQRCode(encryptedValue)
        } catch (e: Exception) {
            _errorMessage.value = ENCRYPTION_ERROR
        }
    }
}