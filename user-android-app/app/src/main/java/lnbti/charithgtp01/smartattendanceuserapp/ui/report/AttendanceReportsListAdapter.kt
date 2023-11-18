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
import javax.inject.Inject

/**
 * User Fragment List Adapter
 */
class AttendanceReportsListAdapter @Inject constructor() :
    ListAdapter<AttendanceDate, AttendanceReportsListAdapter.AttendanceReportsListViewHolder>(
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
        getItem(position).apply {
            holder.binding.apply {
                date.apply {
                    val dateString =
                        "${dayOfMonth}/${month}/${year}"
                    tvDate.text = dateString
                    // Highlight weekends
                    when {
                        isWeekend -> mainLayout.setCardBackgroundColor(
                            ContextCompat.getColor(
                                holder.itemView.context,
                                R.color.weekend_highlighted_color
                            )
                        )
                        else -> mainLayout.setCardBackgroundColor(Color.WHITE)
                    }
                }


                when (status) {
                    AttendanceStatus.PRESENT -> {
                        tvStatus.text = "Present"
                        tvStatus.setBackgroundResource(R.drawable.green_button_bg)
                    }

                    AttendanceStatus.ABSENT -> {
                        tvStatus.text = "Absent"
                        tvStatus.setBackgroundResource(R.drawable.red_button_bg)
                    }

                    AttendanceStatus.UNKNOWN -> {
                        tvStatus.text = "Unknown"
                        tvStatus.setBackgroundResource(R.drawable.yellow_button_bg)
                    }
                }
            }
        }

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

