package lnbti.charithgtp01.smartattendanceadminapp.ui.settings

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.model.SettingsObject
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils
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
                R.mipmap.other_settings,
                userRepository.context.getString(R.string.other_settings)
            ),
            SettingsObject(
                R.mipmap.change_password,
                userRepository.context.getString(R.string.change_password)
            )
        )
        _settingsList.value = allSettings

    }
}