package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapprovals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lnbti.charithgtp01.smartattendanceadminapp.databinding.LayoutPendingApprovalsListBinding
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import javax.inject.Inject

/**
 * Pending Approval Fragment List Adapter
 */
class PendingApprovalListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<User, PendingApprovalListAdapter.PendingApprovalListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingApprovalListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutPendingApprovalsListBinding.inflate(inflater, parent, false)
        return PendingApprovalListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingApprovalListViewHolder, position: Int) {
        val pendingApproval = getItem(position)
        holder.bind(pendingApproval)
        holder.binding.root.setOnClickListener {
            itemClickListener.itemClick(pendingApproval)
        }
    }

    /**
     * On Item Click Listener
     */
    interface OnItemClickListener {
        fun itemClick(item: User)
    }

    inner class PendingApprovalListViewHolder(val binding: LayoutPendingApprovalsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pendingApproval: User) {
            binding.setVariable(BR.item, pendingApproval)
            binding.executePendingBindings()
        }
    }
}

/**
 * Diff Util Interface
 */
val diffUtil = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.firstName == newItem.firstName
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

