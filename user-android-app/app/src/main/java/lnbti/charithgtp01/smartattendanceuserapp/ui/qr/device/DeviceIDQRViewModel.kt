package lnbti.charithgtp01.smartattendanceuserapp.ui.qr.device

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.generateQRCodeBitmap
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class DeviceIDQRViewModel @Inject constructor() : ViewModel() {

    private val _generatedQRCodeData = MutableLiveData<Bitmap>()
    val generatedQRCodeData
        get() = _generatedQRCodeData

    private val _deviceID = MutableLiveData<String>()
    val deviceID
        get() = _deviceID

    fun generateQRCode(text: String) {
        _deviceID.value = text

        // Implement your QR code generation logic here and set the result in _generatedQRCodeData
        generateQRCodeBitmap(text).apply {
            _generatedQRCodeData.value = this
        }
    }
}