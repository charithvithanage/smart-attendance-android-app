package lnbti.charithgtp01.smartattendanceuserapp.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentProfileBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils

/**
 * Users Fragment
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var binding: FragmentProfileBinding? = null
    private lateinit var viewModel: ProfileViewModel
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
         * Initiate Data Binding and View Model
        */
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initiateProgressDialog()
        viewModelObservers()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
        viewModel.profileResult.observe(requireActivity()) {
            val apiResult = it

            dialog?.dismiss()

            val gson = Gson()
            if (apiResult?.data != null) {
                val user = gson.fromJson(apiResult.data.data.toString(), User::class.java)
                viewModel.setUser(user)
                /* Show profile icon using Glide */
                binding?.ownerIconView?.let { Glide.with(this).load(user.avatar).into(it) }
            }

        }
    }

    /**
     * Progress Dialog Initiation
     */
    private fun initiateProgressDialog() {
        dialog = DialogUtils.showProgressDialog(requireContext(), getString(R.string.wait))
    }

}