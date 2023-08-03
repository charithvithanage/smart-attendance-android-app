package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceDate
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceStatus
import lnbti.charithgtp01.smartattendanceuserapp.model.CalendarDate
import lnbti.charithgtp01.smartattendanceuserapp.repositories.UserRepository
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
class AttendanceReportViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

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

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat(
            userRepository.context.getString(R.string.date_format),
            Locale.getDefault()
        )
        return format.format(date)
    }

    fun setDate(startDate: Date, endDate: Date) {
        _startDateString.value = formatDate(startDate)
        _endDateString.value = formatDate(endDate)
        _calendarDates.value = generateCalendarDates(startDate, endDate)
    }


}