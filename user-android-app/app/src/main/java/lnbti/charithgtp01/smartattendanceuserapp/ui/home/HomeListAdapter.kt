package lnbti.charithgtp01.smartattendanceuserapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.ANDROID_USER
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.OTHER
import lnbti.charithgtp01.smartattendanceuserapp.databinding.LayoutHomeListBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import javax.inject.Inject

/**
 * Adapter for displaying a list of [User] items in the home screen.
 *
 * This adapter extends [ListAdapter] and uses a [DiffUtil] callback for efficient item updates.
 *
 * @property itemClickListener The click listener for handling item interactions.
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
        holder.apply {
            getItem(position).apply {
                //QR generate and should scan from the Employee
                if (userType == ANDROID_USER) {
                    binding.btnProceed.text =
                        holder.binding.root.context.getString(R.string.generate)
                } else if (userType == OTHER) {
                    //Other devices users has printed QR
                    //Office User cam scan printed QR
                    binding.btnProceed.text =
                        holder.binding.root.context.getString(R.string.scan)
                }
                bind(this)
            }
        }
    }

    /**
     * Interface definition for handling item clicks.
     */
    interface OnItemClickListener {
        /**
         * Handles the event when a user item should be scanned.
         *
         * @param item The [User] item to be scanned.
         */
        fun scan(item: User)

        /**
         * Handles the event when a user item should be generated.
         *
         * @param item The [User] item to be generated.
         */
        fun generate(item: User)
    }

    inner class HomeListViewHolder(val binding: LayoutHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                setVariable(BR.item, user)
                //QR generate and should scan from the Employee
                btnProceed.setOnClickListener {
                    when (user.userType) {
                        ANDROID_USER -> itemClickListener.generate(user)
                        else -> itemClickListener.scan(user)
                    }
                    //Other devices users has printed QR
                    //Office User cam scan printed QR
                }

                executePendingBindings()
            }
        }
    }
}

/**
 * Diff Util callback for the [HomeListAdapter].
 */
val diffUtil = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.firstName == newItem.firstName
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

