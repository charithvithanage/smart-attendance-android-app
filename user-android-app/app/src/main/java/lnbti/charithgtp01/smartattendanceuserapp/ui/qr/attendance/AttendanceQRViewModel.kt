package lnbti.charithgtp01.smartattendanceuserapp.ui.qr.attendance

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.generateQRCodeBitmap
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel class for handling the generation of QR codes for attendance tracking.
 *
 * @property _generatedQRCodeData LiveData holding the generated QR code represented as a [Bitmap].
 * @property generatedQRCodeData Public accessor for [_generatedQRCodeData].
 * @property _selectedUser LiveData holding the selected user for attendance tracking.
 * @property selectedUser Public accessor for [_selectedUser].
 * @property today Current date used for attendance tracking.
 * @property _dateString LiveData holding the formatted date string.
 * @property dateString Public accessor for [_dateString].
 */
@HiltViewModel
class AttendanceQRViewModel @Inject constructor() : ViewModel() {

    private val _generatedQRCodeData = MutableLiveData<Bitmap>()
    val generatedQRCodeData
        get() = _generatedQRCodeData

    private val _selectedUser = MutableLiveData<User>()
    val selectedUser
        get() = _selectedUser

    private var today: Date = Date()

    private val _dateString = MutableLiveData<String>()
    val dateString get() = _dateString

    /**
     * Generates a QR code bitmap based on the provided [text] and [selectedUser].
     *
     * The generated QR code is stored in [_generatedQRCodeData].
     *
     * @param text The QR code value.
     * @param selectedUser The user for whom the QR code is generated.
     */
    fun generateQRCode(text: String?, selectedUser: User) {
        _selectedUser.value = selectedUser
        _dateString.value = Utils.formatDate(today)
        text?.let {
            generateQRCodeBitmap(text).apply {
                _generatedQRCodeData.value = this
            }
        }
    }
}