package lnbti.charithgtp01.smartattendanceuserapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.model.User
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