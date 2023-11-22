package lnbti.charithgtp01.smartattendanceuserapp.ui.othersettings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for handling other settings in the application.
 *
 * This class is annotated with [@HiltViewModel] to enable dependency injection with Hilt.
 *
 * @constructor Creates an instance of [OtherSettingsViewModel].
 */
@HiltViewModel
class OtherSettingsViewModel @Inject constructor() : ViewModel() {
    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    private val biometricEnabledLiveData = MutableLiveData<Boolean>()

    /**
     * Retrieves the LiveData for observing the status of biometric authentication.
     *
     * @return [LiveData] for observing biometric authentication status.
     */
    fun getBiometricEnabledLiveData(): LiveData<Boolean> {
        return biometricEnabledLiveData
    }

    /**
     * Sets the value for biometric authentication status, updating the associated LiveData.
     *
     * @param value The new value for biometric authentication status.
     */
    fun setBiometricEnabled(value: Boolean) {
        biometricEnabledLiveData.value = value
    }

}