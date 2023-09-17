package lnbti.charithgtp01.smartattendanceuserapp.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
class ProfileViewModel @Inject constructor() : ViewModel() {

    //Profile Live Data
    private val _profile = MutableLiveData<User?>()
    val profile: LiveData<User?> get() = _profile

    fun setUser(user: User) {
        _profile.value = user
    }
}