package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapprovals

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceadminapp.databinding.FragmentPendingApprovalsBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapporvaldetails.PendingApprovalDetailsActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras

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
            if (it != null) {
                DialogUtils.showAlertDialog(
                    requireContext(),
                    Constants.FAIL, it,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {

                        }
                    })
            }

        }

        viewModel.isDialogVisible.observe(requireActivity())
        {
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
        viewModel.pendingApprovalList.observe(requireActivity())
        {
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
                    val gson = Gson()
                    val prefMap = HashMap<String, String>()
                    prefMap[OBJECT_STRING] = gson.toJson(item)
                    navigateToAnotherActivityWithExtras(
                        requireActivity(),
                        PendingApprovalDetailsActivity::class.java,
                        prefMap
                    )
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