package lnbti.charithgtp01.smartattendanceadminapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants.CHANGE_PASSWORD
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants.OTHER_SETTINGS
import lnbti.charithgtp01.smartattendanceadminapp.model.SettingsObject
import lnbti.charithgtp01.smartattendanceadminapp.repositories.UserRepository
import javax.inject.Inject

/**
 * Pending Approval View Model
 */
@HiltViewModel
class SettingsListViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _settingsList = MutableLiveData<List<SettingsObject>>()
    val settingsList: LiveData<List<SettingsObject>> get() = _settingsList

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    lateinit var allSettings: List<SettingsObject>

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

        allSettings = listOf(
            SettingsObject(
                R.mipmap.admin_settings,
                OTHER_SETTINGS
            ),
            SettingsObject(
                R.mipmap.admin_password,
                CHANGE_PASSWORD
            )
        )
        _settingsList.value = allSettings

    }
}