package lnbti.charithgtp01.smartattendanceuserapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lnbti.charithgtp01.smartattendanceuserapp.databinding.LayoutHomeListBinding
import lnbti.charithgtp01.smartattendanceuserapp.databinding.LayoutUserListBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import javax.inject.Inject

/**
 * User Fragment List Adapter
 */
class HomeListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<User, HomeListAdapter.HomeListViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutHomeListBinding.inflate(inflater, parent, false)
        return HomeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        val pendingApproval = getItem(position)

        if (position == 0)
            holder.binding.btnProceed.text = "SCAN"
        else
            holder.binding.btnProceed.text = "GENERATE"

        holder.binding.repositoryNameView.text =
            pendingApproval.first_name + " " + pendingApproval.last_name
        /* Show profile icon using Glide */
        Glide.with(holder.itemView.rootView).load(pendingApproval.avatar)
            .into(holder.binding.ownerIconView)
        holder.binding.btnProceed.setOnClickListener {
            itemClickListener.itemClick(pendingApproval)
        }
    }

    /**
     * On Item Click Listener
     */
    interface OnItemClickListener {
        fun itemClick(item: User)
    }

    inner class HomeListViewHolder(val binding: LayoutHomeListBinding) :
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

