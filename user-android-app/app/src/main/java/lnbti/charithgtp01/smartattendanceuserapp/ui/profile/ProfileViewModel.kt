package lnbti.charithgtp01.smartattendanceuserapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import javax.inject.Inject

/**
 * ViewModel responsible for managing the user profile data.
 *
 * @property _profile Private MutableLiveData holding the user profile information.
 * @property profile Public LiveData exposing the user profile information. Observers can listen to changes in the profile using this LiveData.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    // Profile LiveData
    private val _profile = MutableLiveData<User?>()

    /**
     * Public LiveData representing the user profile.
     * Observers can observe changes to the user profile using this LiveData.
     */
    val profile: LiveData<User?> get() = _profile

    /**
     * Set the user profile information.
     *
     * @param user The user object containing profile information to be set.
     */
    fun setUser(user: User) {
        _profile.value = user
    }
}