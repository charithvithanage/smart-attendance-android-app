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
import lnbti.charithgtp01.smartattendanceadminapp.constants.MessageConstants
import lnbti.charithgtp01.smartattendanceadminapp.utils.NetworkUtils

/**
 * Users Fragment View Model
 */
@HiltViewModel
class UserEditViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    lateinit var nic: String
    lateinit var deviceID: String
    private var userStatus: Boolean = false
    private lateinit var userRole: String
    lateinit var dob: String
    lateinit var gender: String
    lateinit var email: String
    lateinit var firstName: String
    lateinit var lastName: String

    private val _gender = MutableLiveData<String>()

    val selectedGender: LiveData<String>
        get() = _gender

    //Gender Radio Button value check
    var isLeftGenderButtonChecked = false
    var isRightGenderButtonChecked = false

    //UserStatus Radio Button value check
    var isLeftStatusButtonChecked = false
    var isRightStatusButtonChecked = false

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

        setGenderValues(gender)
        setStatusValues(user.getUserStatusString())
    }

    /**
     * Select left and right radio button according to gender value
     */
    fun setGenderValues(selectedValue: String) {
        gender = selectedValue
        if (selectedValue == "Male") {
            isLeftGenderButtonChecked = true
            isRightGenderButtonChecked = false
        } else {
            isLeftGenderButtonChecked = false
            isRightGenderButtonChecked = true
        }
    }

    /**
     * Select left and right radio button according to status value
     */
    fun setStatusValues(selectedValue: String) {
        if (selectedValue == "Active") {
            userStatus = true
            isLeftStatusButtonChecked = true
            isRightStatusButtonChecked = false
        } else {
            userStatus = false
            isLeftStatusButtonChecked = false
            isRightStatusButtonChecked = true
        }
    }

    // Function to set the selected item
    fun selectUserRole(item: String) {
        userRole = item
    }

    fun updateUser() {
        //If Network available call to backend API
        if (NetworkUtils.isNetworkAvailable()) {
            //Show Progress Dialog when click on the search view submit button
            _isDialogVisible.value = true
            /* View Model Scope - Coroutine */
            val user = UserUpdateRequest(
                nic,
                email,
                firstName,
                lastName,
                gender,
                userRole,
                dob,
                userStatus,
                deviceID,
                userType = "Android User"
            )

            val gson = Gson()
            Log.d(TAG, "Update Request " + gson.toJson(user))
            viewModelScope.launch {
                // can be launched in a separate asynchronous job
                val result =
                    userRepository.updateUser(
                        user
                    )

                serverResult.value = result
                _isDialogVisible.value = false
            }
        } else {
            _errorMessage.value = MessageConstants.NO_INTERNET
        }
    }

}