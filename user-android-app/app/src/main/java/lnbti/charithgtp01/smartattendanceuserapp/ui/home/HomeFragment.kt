package lnbti.charithgtp01.smartattendanceuserapp.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentHomeBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.qr.DeviceIDQRActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.userdetails.UserDetailsActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras

/**
 * Users Fragment
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var usersListAdapter: HomeListAdapter
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
         * Initiate Data Binding and View Model
        */
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
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
                object : DialogButtonClickListener {
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
            HomeListAdapter(object : HomeListAdapter.OnItemClickListener {
                override fun scan(item: User) {


                }

                override fun generate(item: User) {
                    val gson = Gson()
                    val prefMap = HashMap<String, String>()
                    prefMap[Constants.OBJECT_STRING] = gson.toJson(item)
                    navigateToAnotherActivityWithExtras(
                        requireActivity(),
                        DeviceIDQRActivity::class.java,
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