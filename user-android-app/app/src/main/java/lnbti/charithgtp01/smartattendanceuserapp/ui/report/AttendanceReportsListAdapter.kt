package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.databinding.LayoutAttendanceReportListBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceDate
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceStatus
import lnbti.charithgtp01.smartattendanceuserapp.model.CalendarDate
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import javax.inject.Inject

/**
 * User Fragment List Adapter
 */
class AttendanceReportsListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<AttendanceDate, AttendanceReportsListAdapter.AttendanceReportsListViewHolder>(
    diffUtil
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceReportsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAttendanceReportListBinding.inflate(inflater, parent, false)
        return AttendanceReportsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceReportsListViewHolder, position: Int) {
        val attendanceDate = getItem(position)
        val calendarDate = attendanceDate.date
        val dateString = "${calendarDate.dayOfMonth}/${calendarDate.month}/${calendarDate.year}"
        holder.binding.tvDate.text = dateString
        // Highlight weekends
        if (calendarDate.isWeekend) {
            holder.binding.mainLayout.setCardBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.weekend_highlighted_color
                )
            )
        } else {
            holder.binding.mainLayout.setCardBackgroundColor(Color.WHITE)
        }

        when (attendanceDate.status) {
            AttendanceStatus.PRESENT -> {
                holder.binding.tvStatus.text = "Present"
                holder.binding.tvStatus.setBackgroundResource(R.drawable.green_button_bg)
            }

            AttendanceStatus.ABSENT -> {
                holder.binding.tvStatus.text = "Absent"
                holder.binding.tvStatus.setBackgroundResource(R.drawable.red_button_bg)
            }

            AttendanceStatus.UNKNOWN -> {
                holder.binding.tvStatus.text = "Unknown"
                holder.binding.tvStatus.setBackgroundResource(R.drawable.yellow_button_bg)
            }
        }
    }

    /**
     * On Item Click Listener
     */
    interface OnItemClickListener {
        fun itemClick(item: AttendanceDate)
    }

    inner class AttendanceReportsListViewHolder(val binding: LayoutAttendanceReportListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


}

/**
 * Diff Util Interface
 */
val diffUtil = object : DiffUtil.ItemCallback<AttendanceDate>() {
    override fun areItemsTheSame(oldItem: AttendanceDate, newItem: AttendanceDate): Boolean {
        return oldItem.date.dayOfMonth == newItem.date.dayOfMonth
    }

    override fun areContentsTheSame(oldItem: AttendanceDate, newItem: AttendanceDate): Boolean {
        return oldItem == newItem
    }
}

