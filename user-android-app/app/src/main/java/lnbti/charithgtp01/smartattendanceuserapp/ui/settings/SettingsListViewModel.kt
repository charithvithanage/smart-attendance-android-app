package lnbti.charithgtp01.smartattendanceuserapp.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.SettingsObject
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import javax.inject.Inject

/**
 * Pending Approval View Model
 */
@HiltViewModel
class SettingsListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val context: Context
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
                context.getString(R.string.get_device_id)
            ),
            SettingsObject(
                R.mipmap.other_settings,
                context.getString(R.string.other_settings)
            ),
            SettingsObject(
                R.mipmap.change_password,
                context.getString(R.string.change_password)
            )
        )
        _settingsList.value = allUsersList

    }
}