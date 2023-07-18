package lnbti.charithgtp01.smartattendanceadminapp.ui.users

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lnbti.charithgtp01.smartattendanceadminapp.databinding.FragmentUsersBinding

/**
 * Users Fragment
 */
class UsersFragment : Fragment() {

    private var binding: FragmentUsersBinding? = null
    private lateinit var viewModel: UsersViewModel
    private lateinit var pendingApprovalListAdapter: PendingApprovalListAdapter
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
         * Initiate Data Binding and View Model
        */
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[UsersViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
        return binding?.root
    }


}