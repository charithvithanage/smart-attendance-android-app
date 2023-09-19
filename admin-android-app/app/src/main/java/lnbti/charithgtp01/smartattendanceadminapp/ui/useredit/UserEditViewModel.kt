package lnbti.charithgtp01.smartattendanceadminapp.ui.useredit

import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceadminapp.model.ApiCallResponse
import lnbti.charithgtp01.smartattendanceadminapp.model.ApprovalRequest
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.model.UserUpdateRequest
import lnbti.charithgtp01.smartattendanceadminapp.repositories.ApprovalRepository
import lnbti.charithgtp01.smartattendanceadminapp.repositories.UserRepository
import javax.inject.Inject
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

/**
 * Users Fragment View Model
 */
@HiltViewModel
class UserEditViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    lateinit var nic: String
    lateinit var deviceID: String
    private var userStatus: Boolean = false
    lateinit var userRole: String
    lateinit var dob: String
    var gender: String="Male"
    lateinit var email: String
    lateinit var firstName: String
    lateinit var lastName: String

    private val _gender = MutableLiveData<String>()

    val selectedGender: LiveData<String>
        get() = _gender

    init {
        // Initialize the gender property with the default value
        _gender.value = "Male"
    }

    //Gender Radio Button value check
    var isLeftButtonChecked = false
    var isRightButtonChecked = false

    private val _pendingApprovalUser = MutableLiveData<User>()
    val pendingApprovalUser: LiveData<User> get() = _pendingApprovalUser

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    //Server response live data
    private val _serverResult = MutableLiveData<ApiCallResponse?>()
    val serverResult: MutableLiveData<ApiCallResponse?> = _serverResult

    init {
        gender="Male"
    }
    /**
     * Set User Object to Live Data
     * @param Selected Pending Approval User Object
     */
    fun setUserData(user: User) {
        _pendingApprovalUser.value = user
        nic = user.nic
        deviceID = user.deviceID
        userStatus = user.userStatus
        userRole = user.userRole
        dob = user.dob
        gender = user.gender
        email = user.email
        firstName = user.firstName
        lastName = user.lastName
        setGenderValues(user.gender)
    }


    fun setSelectedGenderRadioButtonValue(selectedValue: String) {
        gender = selectedValue
        setGenderValues(selectedValue)
    }

    /**
     * Select left and right radio button according to gender value
     */
    private fun setGenderValues(selectedValue: String) {
//        if (selectedValue == "Male") {
//            isLeftButtonChecked = true
//            isRightButtonChecked = false
//        } else {
//            isLeftButtonChecked = false
//            isRightButtonChecked = true
//        }
    }

    // Function to set the selected item
    fun selectUserRole(item: String) {
        userRole = item
    }

    fun updateUser() {
        val user = UserUpdateRequest(
            nic,
            email,
            firstName,
            lastName,
            gender,
            userRole,
            dob,
            true,
            deviceID
        )

        val gson = Gson()
        Log.d(TAG,"Update Request "+ gson.toJson(user))
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result =
                userRepository.updateUser(
                    user
                )

            serverResult.value = result
        }
    }

}