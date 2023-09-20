package lnbti.charithgtp01.smartattendanceuserapp.ui.qr.attendance

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import java.util.Date
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class AttendanceQRViewModel @Inject constructor() : ViewModel() {

    private val _generatedQRCodeData = MutableLiveData<Bitmap>()
    val generatedQRCodeData: LiveData<Bitmap>
        get() = _generatedQRCodeData

    private val _selectedUser = MutableLiveData<User>()
    val selectedUser: LiveData<User>
        get() = _selectedUser

    private var today: Date = Date()

    private val _dateString = MutableLiveData<String>()
    val dateString: LiveData<String> get() = _dateString

    // ViewModel logic and data manipulation can be defined here

    fun generateQRCode(text: String?, selectedUser: User) {
        _selectedUser.value = selectedUser
        _dateString.value = Utils.formatDate(today)
        if (text != null) {
            // Implement your QR code generation logic here and set the result in _generatedQRCodeData
            val generatedQRCode = generateQRCodeBitmap(text)
            _generatedQRCodeData.value = generatedQRCode
        }
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
}