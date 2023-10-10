package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lnbti.charithgtp01.smartattendanceuserapp.databinding.LayoutAttendanceDataReportListBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceData
import javax.inject.Inject

/**
 * User Fragment List Adapter
 */
class AttendanceDataReportsListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<AttendanceData, AttendanceDataReportsListAdapter.AttendanceDataReportsListViewHolder>(
    attendanceDataReportDiffUtil
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceDataReportsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAttendanceDataReportListBinding.inflate(inflater, parent, false)
        return AttendanceDataReportsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceDataReportsListViewHolder, position: Int) {
        val attendanceDate = getItem(position)
        holder.bind(attendanceDate)
    }

    /**
     * On Item Click Listener
     */
    interface OnItemClickListener {
        fun itemClick(item: AttendanceData)
    }

    inner class AttendanceDataReportsListViewHolder(val binding: LayoutAttendanceDataReportListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: AttendanceData) {
            binding.setVariable(BR.item, user)
            binding.executePendingBindings()
        }
    }


}

/**
 * Diff Util Interface
 */
val attendanceDataReportDiffUtil = object : DiffUtil.ItemCallback<AttendanceData>() {
    override fun areItemsTheSame(oldItem: AttendanceData, newItem: AttendanceData): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: AttendanceData, newItem: AttendanceData): Boolean {
        return oldItem == newItem
    }
}

