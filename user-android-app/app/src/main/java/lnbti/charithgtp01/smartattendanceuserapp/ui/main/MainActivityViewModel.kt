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

    fun setDialogVisibility(status: Boolean) {
        Log.d("DIALOG TEST", status.toString())
        _isDialogVisible.value = status
    }
}