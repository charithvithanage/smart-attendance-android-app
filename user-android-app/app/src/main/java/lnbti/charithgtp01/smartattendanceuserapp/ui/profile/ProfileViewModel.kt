package lnbti.charithgtp01.smartattendanceuserapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.JSONResource
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    private val _profileResult = MutableLiveData<JSONResource>()
    val profileResult: LiveData<JSONResource> get() = _profileResult

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    //Profile Live Data
    private val _profile = MutableLiveData<User?>()
    val profile: LiveData<User?> get() = _profile

    init {
        getUser()
    }

    fun setUser(user: User) {
        _profile.value = user
        _isDialogVisible.value = false
    }

    private fun getUser() {
        val isNetworkAvailable = Utils.isOnline(userRepository.context.applicationContext)

        //If Network available call to backend API
        if (isNetworkAvailable) {
            //Show Progress Dialog when click on the search view submit button
            _isDialogVisible.value = true
            /* View Model Scope - Coroutine */
            viewModelScope.launch {
                val result = userRepository.getUserFromDataSource()

                if (result.data != null) {
                    _profileResult.value = result
                } else
                    _errorMessage.value = result.error?.error
            }
        } else {
            //Show Error Alert
            _errorMessage.value = userRepository.context.getString(R.string.no_internet)
        }
    }


}