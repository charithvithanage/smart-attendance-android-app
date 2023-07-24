package lnbti.charithgtp01.smartattendanceadminapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.databinding.FragmentSettingsBinding
import lnbti.charithgtp01.smartattendanceadminapp.model.SettingsObject
import lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapprovals.PendingApprovalDetailsActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null
    private lateinit var viewModel: SettingsListViewModel
    private lateinit var settingsAdapterListAdapter: SettingsAdapterListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
        * Initiate Data Binding and View Model
       */
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SettingsListViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
        return binding?.root
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
        /* Observer to catch list data
      * Update Recycle View Items using Diff Utils
      */
        viewModel.settingsList.observe(requireActivity()) {
            settingsAdapterListAdapter.submitList(it)
        }
    }

    /**
     * Recycle View data configuration
     */
    private fun initiateAdapter() {
        /* Initiate Adapter */
        settingsAdapterListAdapter =
            SettingsAdapterListAdapter(object : SettingsAdapterListAdapter.OnItemClickListener {
                override fun itemClick(item: SettingsObject) {
                    val gson = Gson()
                    val prefMap = HashMap<String, String>()
                    prefMap[Constants.OBJECT_STRING] = gson.toJson(item)
                    Utils.navigateToAnotherActivityWithExtras(
                        requireActivity(),
                        PendingApprovalDetailsActivity::class.java,
                        prefMap
                    )
                }
            })

        /* Set Adapter to Recycle View */
        binding?.recyclerView.also { it2 ->
            it2?.adapter = settingsAdapterListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}