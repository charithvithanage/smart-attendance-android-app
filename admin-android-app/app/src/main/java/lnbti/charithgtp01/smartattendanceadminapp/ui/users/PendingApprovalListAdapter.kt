package lnbti.charithgtp01.smartattendanceadminapp.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lnbti.charithgtp01.smartattendanceadminapp.databinding.LayoutPendingApprovalsListBinding
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import javax.inject.Inject

/**
 * Main Activity List Adapter
 */
class PendingApprovalListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<User, PendingApprovalListAdapter.UsersListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutPendingApprovalsListBinding.inflate(inflater, parent, false)
        return UsersListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
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

    inner class UsersListViewHolder(val binding: LayoutPendingApprovalsListBinding) :
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

