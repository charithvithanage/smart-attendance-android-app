package lnbti.charithgtp01.smartattendanceadminapp.ui.othersettings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.initiateActionBar
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class OtherSettingsViewModel @Inject constructor() : ViewModel() {
    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

}