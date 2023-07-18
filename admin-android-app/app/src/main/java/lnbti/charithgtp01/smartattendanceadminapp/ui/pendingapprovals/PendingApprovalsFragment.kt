package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapprovals

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.databinding.FragmentPendingApprovalsBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ErrorDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.ui.users.PendingApprovalListAdapter
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils

class PendingApprovalsFragment : Fragment() {
    private var binding: FragmentPendingApprovalsBinding? = null
    private lateinit var viewModel: PendingApprovalsViewModel
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
        binding = FragmentPendingApprovalsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[PendingApprovalsViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateAdapter()
        initiateProgressDialog()
        viewModelObservers()
    }

    /**
     * Live Data Updates
     */
    private fun viewModelObservers() {
        /* Show error message in the custom error dialog */
        viewModel.errorMessage.observe(requireActivity()) {
            DialogUtils.showErrorDialog(
                requireContext(),
                it,
                object : ErrorDialogButtonClickListener {
                    override fun onButtonClick() {

                    }
                })
        }

        viewModel.isDialogVisible.observe(requireActivity()) {
            if (it) {
                /* Show dialog when calling the API */
                dialog?.show()
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }

        /* Observer to catch list data
        * Update Recycle View Items using Diff Utils
        */
        viewModel.gitHubRepoList.observe(requireActivity()) {
            pendingApprovalListAdapter.submitList(it)
        }
    }

    /**
     * Progress Dialog Initiation
     */
    private fun initiateProgressDialog() {
        dialog = DialogUtils.showProgressDialog(context, context?.getString(R.string.wait))
    }

    /**
     * Recycle View data configuration
     */
    private fun initiateAdapter() {
        /* Initiate Adapter */
        pendingApprovalListAdapter =
            PendingApprovalListAdapter(object : PendingApprovalListAdapter.OnItemClickListener {
                override fun itemClick(item: User) {
//                gotoRepositoryFragment(item)
                }
            })

        /* Set Adapter to Recycle View */
        binding?.recyclerView.also { it2 ->
            it2?.adapter = pendingApprovalListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}