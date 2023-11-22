package lnbti.charithgtp01.smartattendanceuserapp.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the main activity of your application.
 *
 * This ViewModel is responsible for managing the state and data related to the main activity,
 * including the currently displayed fragment, the status of the bottom menu, expanded states,
 * and whether search results are empty.
 *
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor() :
    ViewModel() {

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible get() = _isDialogVisible

    private val _shouldOpenSettings = MutableLiveData(false)
    val shouldOpenSettings get() = _shouldOpenSettings

    private val _checkPermission = MutableLiveData(false)
    val checkPermission get() = _checkPermission

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    fun setDialogVisibility(status: Boolean) {
        Log.d("DIALOG TEST", status.toString())
        _isDialogVisible.value = status
    }

    fun openSettings() {
        _shouldOpenSettings.value = true
    }

    fun checkPermission() {
        _checkPermission.value = true
    }


    /**
     * Sets the error message for the ViewModel.
     *
     * @param errorMessage The error message to be set.
     */
    fun setErrorMessage(errorMessage: String?) {
        _errorMessage.value = errorMessage
    }
}