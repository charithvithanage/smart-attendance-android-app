package lnbti.charithgtp01.smartattendanceuserapp.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lnbti.charithgtp01.smartattendanceuserapp.databinding.LayoutUserListBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.home.HomeListAdapter
import javax.inject.Inject

/**
 * User Fragment List Adapter
 */
class UsersListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<User, UsersListAdapter.UsersListViewHolder>(lnbti.charithgtp01.smartattendanceuserapp.ui.home.diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutUserListBinding.inflate(inflater, parent, false)
        return UsersListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
        val pendingApproval = getItem(position)
        holder.binding.repositoryNameView.text =
            pendingApproval.firstName + " " + pendingApproval.lastName

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

    inner class UsersListViewHolder(val binding: LayoutUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}

/**
 * Diff Util Interface
 */
val diffUtil = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.firstName == newItem.lastName
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

