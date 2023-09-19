<<<<<<< .mine
package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.isOnline
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class AttendanceReportViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>> get() = _usersList

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    lateinit var allUsersList: List<User>

    /**
     * This will call when the View Model Created
     */
    init {
//        getUsersList()
    }

    /**
     * Get Server Response and Set values to live data
     */
    private fun getUsersList() {

        val isNetworkAvailable = isOnline(userRepository.context.applicationContext)

        //If Network available call to backend API
        if (isNetworkAvailable) {
            //Show Progress Dialog when click on the search view submit button
            _isDialogVisible.value = true
            /* View Model Scope - Coroutine */
            viewModelScope.launch {
                val resource = userRepository.getUsersFromDataSource()

                if (resource?.data != null) {
                    allUsersList = resource.data.data
                    _usersList.value = allUsersList
                } else
                    _errorMessage.value = resource?.error?.error

                /* Hide Progress Dialog with 1 Second delay after fetching the data list from the server */
                delay(1000L)
                _isDialogVisible.value = false
            }
        } else {
            //Show Error Alert
            _errorMessage.value = userRepository.context.getString(R.string.no_internet)
        }

    }

    /**
     * @param searchString Search View entered value
     * @return Data list filtered by user's full name
     */
    private fun filterApprovalList(searchString: String): List<User>? {
        // to get the result as list
        return allUsersList?.filter { s ->
            (s.first_name + " " + s.last_name).contains(
                searchString
            )
        }
    }

}










































=======
package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceDate
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceStatus
import lnbti.charithgtp01.smartattendanceuserapp.model.CalendarDate
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getLastDayOfMonth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Users Fragment View Model
 */
@HiltViewModel
class AttendanceReportViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val context: Context
) :
    ViewModel() {

    //Users Dropdown values
    val spinnerOptions = arrayOf("George Bluth", "Janet Weaver", "Emma Wong","Eve Holt","Charles Morris","Tracey Ramos")
    var selectedSpinnerPosition: Int = 0

    //Start Date Live Date
    private val _startDateString = MutableLiveData<String>()
    val startDateString: LiveData<String> get() = _startDateString

    //End Date Live Date
    private val _endDateString = MutableLiveData<String>()
    val endDateString: LiveData<String> get() = _endDateString


    private val _calendarDates = MutableLiveData<List<AttendanceDate>>()
    val calendarDates: LiveData<List<AttendanceDate>> get() = _calendarDates

    //Dialog Visibility Live Data
    private val _isDialogVisible = MutableLiveData<Boolean>()
    val isDialogVisible: LiveData<Boolean> get() = _isDialogVisible

    //Error Message Live Data
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage


    /**
     * This will call when the View Model Created
     */
    init {
        val startDate = Date()
        val endDate: Date = getLastDayOfMonth(startDate)
        _startDateString.value = formatDate(startDate)
        _endDateString.value = formatDate(endDate)
        _calendarDates.value = generateCalendarDates(startDate, endDate)
    }

    private fun generateCalendarDates(startDate: Date, endDate: Date): List<AttendanceDate> {
        // Replace this with your logic to generate calendar dates and attendance status based on your requirements
        val attendanceDates = mutableListOf<AttendanceDate>()
        val calendar = Calendar.getInstance()

        calendar.time = startDate
        while (calendar.time <= endDate) {
            val isWeekend = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                    calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY

            val isFriday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY

            val calendarDate = CalendarDate(
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH),
                month = calendar.get(Calendar.MONTH) + 1,
                year = calendar.get(Calendar.YEAR),
                isWeekend = isWeekend
            )
            var attendanceDate: AttendanceDate

            /**
             * This part is only for the demo
             */

            //Fridays should be shown as Absent
            if (isFriday) {
                attendanceDate = AttendanceDate(
                    date = calendarDate,
                    status = AttendanceStatus.ABSENT
                )
            } else if (isWeekend) {
                //Weekends should be shown as Unknown
                attendanceDate = AttendanceDate(
                    date = calendarDate,
                    status = AttendanceStatus.UNKNOWN
                )
            } else {
                //Other days should be shown as Present
                attendanceDate = AttendanceDate(
                    date = calendarDate,
                    status = AttendanceStatus.PRESENT
                )
            }



            attendanceDates.add(attendanceDate)

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return attendanceDates
    }


    fun setDate(startDate: Date, endDate: Date) {
        _startDateString.value = formatDate(startDate)
        _endDateString.value = formatDate(endDate)
        _calendarDates.value = generateCalendarDates(startDate, endDate)
    }


}
>>>>>>> .theirs
