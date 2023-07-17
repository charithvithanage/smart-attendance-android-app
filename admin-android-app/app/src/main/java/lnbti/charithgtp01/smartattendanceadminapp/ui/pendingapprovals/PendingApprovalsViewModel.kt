package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapprovals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PendingApprovalsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}