package lnbti.charithgtp01.smartattendanceuserapp.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lnbti.charithgtp01.smartattendanceuserapp.databinding.LayoutSettingsListBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.SettingsObject
import javax.inject.Inject

/**
 * Pending Approval Fragment List Adapter
 */
class SettingsAdapterListAdapter @Inject constructor(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<SettingsObject, lnbti.charithgtp01.smartattendanceuserapp.ui.settings.SettingsAdapterListAdapter.UserSettingsAdapterListViewHolder>(lnbti.charithgtp01.smartattendanceuserapp.settings.diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserSettingsAdapterListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutSettingsListBinding.inflate(inflater, parent, false)
        return UserSettingsAdapterListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserSettingsAdapterListViewHolder, position: Int) {
        val pendingApproval = getItem(position)
        holder.binding.nameView.text =
            pendingApproval.name
        /* Show profile icon using Glide */
        Glide.with(holder.itemView.rootView).load(pendingApproval.icon)
            .into(holder.binding.iconView)
        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(pendingApproval)
        }
    }

    /**
     * On Item Click Listener
     */
    interface OnItemClickListener {
        fun itemClick(item: SettingsObject)
    }

    inner class UserSettingsAdapterListViewHolder(val binding: LayoutSettingsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}

/**
 * Diff Util Interface
 */
val diffUtil = object : DiffUtil.ItemCallback<SettingsObject>() {
    override fun areItemsTheSame(oldItem: SettingsObject, newItem: SettingsObject): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: SettingsObject, newItem: SettingsObject): Boolean {
        return oldItem == newItem
    }
}

