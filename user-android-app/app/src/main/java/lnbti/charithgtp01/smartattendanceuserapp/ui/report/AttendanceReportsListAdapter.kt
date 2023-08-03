package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lnbti.charithgtp01.smartattendanceuserapp.databinding.LayoutUserListBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import javax.inject.Inject

/**
 * User Fragment List Adapter
 */
class AttendanceReportsListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<User, AttendanceReportsListAdapter.AttendanceReportsListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceReportsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutUserListBinding.inflate(inflater, parent, false)
        return AttendanceReportsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceReportsListViewHolder, position: Int) {
        val pendingApproval = getItem(position)
        holder.binding.repositoryNameView.text =
            pendingApproval.first_name + " " + pendingApproval.last_name
        /* Show profile icon using Glide */
        Glide.with(holder.itemView.rootView).load(pendingApproval.avatar)
            .into(holder.binding.ownerIconView)
        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(pendingApproval)
        }
    }

    /**
     * On Item Click Listener
     */
    interface OnItemClickListener {
        fun itemClick(item: User)
    }

    inner class AttendanceReportsListViewHolder(val binding: LayoutUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}

/**
 * Diff Util Interface
 */
val diffUtil = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.first_name == newItem.first_name
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

