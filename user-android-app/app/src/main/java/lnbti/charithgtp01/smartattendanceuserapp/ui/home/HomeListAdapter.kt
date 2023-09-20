package lnbti.charithgtp01.smartattendanceuserapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.withContext
import lnbti.charithgtp01.smartattendanceuserapp.R
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
        val user = getItem(position)
        //QR generate and should scan from the Employee
        if (user.userType == "Android User") {
            holder.binding.btnProceed.text =
                holder.binding.root.context.getString(R.string.generate)
        } else {
            //Other devices users has printed QR
            //Office User cam scan printed QR
            holder.binding.btnProceed.text =
                holder.binding.root.context.getString(R.string.scan)
        }

        holder.bind(user)
    }

    /**
     * On Item Click Listener
     */
    interface OnItemClickListener {
        fun scan(item: User)
        fun generate(item: User)
    }

    inner class HomeListViewHolder(val binding: LayoutHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.setVariable(BR.item, user)
            //QR generate and should scan from the Employee
            binding.btnProceed.setOnClickListener {
                if (user.userType == "Android User") {
                    itemClickListener.generate(user)
                } else {
                    //Other devices users has printed QR
                    //Office User cam scan printed QR
                    itemClickListener.scan(user)
                }

            }

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

