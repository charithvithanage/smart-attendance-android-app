package lnbti.charithgtp01.smartattendanceuserapp.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentUsersBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.userdetails.UserDetailsActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialogInFragment
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras

/**
 * Users Fragment
 */
@AndroidEntryPoint
class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var viewModel: UsersViewModel
    private lateinit var usersListAdapter: UsersListAdapter
    private var dialog: DialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
         * Initiate Data Binding and View Model
        */
        binding = FragmentUsersBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(requireActivity())[UsersViewModel::class.java]
            vm = viewModel
            lifecycleOwner = this@UsersFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateAdapter()
        viewModelObservers()
    }

    /**
     * Live Data Updates
     */
    private fun viewModelObservers() {
        viewModel.apply {
            /* Show error message in the custom error dialog */
            errorMessage.observe(requireActivity()) {
                showErrorDialogInFragment(
                    this@UsersFragment,
                    it
                )
            }

            isDialogVisible.observe(requireActivity()) {
                when {
                    it -> dialog = DialogUtils.showProgressDialogInFragment(
                        this@UsersFragment,
                        getString(R.string.wait)
                    )

                    else -> dialog?.dismiss()
                }
                /* Show dialog when calling the API */
                /* Dismiss dialog after updating the data list to recycle view */
            }

            /* Observer to catch list data
           * Update Recycle View Items using Diff Utils
           */
            usersList.observe(requireActivity()) { it ->
                //Get Active users
                usersListAdapter.submitList(it.filter { it.userStatus })
            }
        }

    }

    /**
     * Recycle View data configuration
     */
    private fun initiateAdapter() {
        /* Initiate Adapter */
        usersListAdapter =
            UsersListAdapter(object : UsersListAdapter.OnItemClickListener {
                override fun itemClick(item: User) {
                    HashMap<String, String>().apply {
                        this[Constants.OBJECT_STRING] = Gson().toJson(item)
                        navigateToAnotherActivityWithExtras(
                            requireActivity(),
                            UserDetailsActivity::class.java,
                            this
                        )
                    }

                }
            })

        /* Set Adapter to Recycle View */
        binding.recyclerView.also { it2 ->
            it2?.adapter = usersListAdapter
        }
    }
}