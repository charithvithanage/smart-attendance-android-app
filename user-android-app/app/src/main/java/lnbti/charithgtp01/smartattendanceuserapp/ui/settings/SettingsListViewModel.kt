package lnbti.charithgtp01.smartattendanceuserapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceuserapp.model.SettingsObject
import javax.inject.Inject

/**
 * Pending Approval View Model
 */
@HiltViewModel
class SettingsListViewModel @Inject constructor(
) :
    ViewModel() {

    private val _settingsList = MutableLiveData<List<SettingsObject>>()
    val settingsList: LiveData<List<SettingsObject>> get() = _settingsList

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    lateinit var allUsersList: List<SettingsObject>

    /**
     * This will call when the View Model Created
     */
    init {
        getSettingsList()
    }

    /**
     * Get Server Response and Set values to live data
     * @param inputText Pass entered value
     */
    private fun getSettingsList() {

        allUsersList = listOf(
            SettingsObject(
                R.mipmap.device_id,
                ResourceConstants.GET_DEVICE_ID
            ),
            SettingsObject(
                R.mipmap.settings,
                ResourceConstants.OTHER_SETTINGS
            ),
            SettingsObject(
                R.mipmap.change_password,
                ResourceConstants.CHANGE_PASSWORD
            )
        )
        _settingsList.value = allUsersList

    }
}