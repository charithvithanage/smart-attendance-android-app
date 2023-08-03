package lnbti.charithgtp01.smartattendanceuserapp.model

data class CalendarDate(val dayOfMonth: Int,
                        val month: Int,
                        val year: Int,
                        var isWeekend: Boolean = false)

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    UNKNOWN
}
