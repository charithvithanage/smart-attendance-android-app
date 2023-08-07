package lnbti.charithgtp01.smartattendanceadminapp.ui.users

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.databinding.FragmentUsersBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.ui.userdetails.UserDetailsActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras

/**
 * Users Fragment
 */
@AndroidEntryPoint
class UsersFragment : Fragment() {
    private var binding: FragmentUsersBinding? = null
    private lateinit var viewModel: UsersViewModel
    private lateinit var usersListAdapter: UsersListAdapter
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
            DialogUtils.showAlertDialog(
                requireContext(),
                Constants.FAIL, it,
                object : CustomAlertDialogListener {
                    override fun onDialogButtonClicked() {

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
        viewModel.usersList.observe(requireActivity()) {
            usersListAdapter.submitList(it)
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
        usersListAdapter =
            UsersListAdapter(object : UsersListAdapter.OnItemClickListener {
                override fun itemClick(item: User) {
                    val gson = Gson()
                    val prefMap = HashMap<String, String>()
                    prefMap[Constants.OBJECT_STRING] = gson.toJson(item)
                    navigateToAnotherActivityWithExtras(
                        requireActivity(),
                        UserDetailsActivity::class.java,
                        prefMap
                    )
                }
            })

        /* Set Adapter to Recycle View */
        binding?.recyclerView.also { it2 ->
            it2?.adapter = usersListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}