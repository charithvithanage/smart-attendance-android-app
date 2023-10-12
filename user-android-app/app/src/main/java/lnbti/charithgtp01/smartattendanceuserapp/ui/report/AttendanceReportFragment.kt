package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentAttendanceReportBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceData
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.userdetails.UserDetailsActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialogInFragment
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import java.util.Calendar
import java.util.Date

/**
 * Attendance Report Fragment
 */
@AndroidEntryPoint
class AttendanceReportFragment : Fragment() {
    private var binding: FragmentAttendanceReportBinding? = null
    private lateinit var viewModel: AttendanceDataReportViewModel
    private lateinit var attendanceReportsListAdapter: AttendanceDataReportsListAdapter
    private var dialog: DialogFragment? = null

    //Initially Start Date Set to Today and End Date Set to Last Day of the current month
    private var startDate: Date = Date()
    private var endDate: Date = Utils.getLastDayOfMonth(startDate)
    val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
         * Initiate Data Binding and View Model
        */
        binding = FragmentAttendanceReportBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AttendanceDataReportViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initiateAdapter()
        viewModelObservers()
    }

    private fun initView() {
        val userRole = getObjectFromSharedPref(requireContext(), Constants.USER_ROLE)

        /**
         * User filter showing only for back office users
         */
        if (userRole == getString(R.string.employee)) {
            binding?.spinner?.visibility = View.GONE
        }

        binding?.fromLayout?.mainLayout?.setOnClickListener {
            openDatePicker(true)
        }

        binding?.toLayout?.mainLayout?.setOnClickListener {
            openDatePicker(false)
        }
    }

    /**
     * Live Data Updates
     */
    private fun viewModelObservers() {
        /* Show error message in the custom error dialog */
        viewModel.errorMessage.observe(requireActivity()) {
            showErrorDialog(
                requireContext(),
                it
            )
        }

        viewModel.isDialogVisible.observe(requireActivity()) {
            if (it) {
                /* Show dialog when calling the API */
                dialog = showProgressDialogInFragment(this@AttendanceReportFragment, getString(R.string.wait))
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }

        /* Observer to catch list data
        * Update Recycle View Items using Diff Utils
        */
        viewModel.responseResult.observe(requireActivity()) {

            val apiResult = it
            if (apiResult?.success == true) {
                val listType = object : TypeToken<List<AttendanceData>>() {}.type
                val attendanceList: List<AttendanceData> = gson.fromJson(apiResult.data, listType)
                viewModel.setCount(attendanceList.size)
                attendanceReportsListAdapter.submitList(attendanceList)
            } else if (apiResult?.data != null) {
                DialogUtils.showErrorDialog(requireActivity(), apiResult.data.toString())
            }

//            dialog?.dismiss()
        }
    }

    private lateinit var loggedInUser: User

    /**
     * Recycle View data configuration
     */
    private fun initiateAdapter() {
        loggedInUser = gson.fromJson(
            getObjectFromSharedPref(requireActivity(), Constants.LOGGED_IN_USER),
            User::class.java
        )
        viewModel.getAttendancesByUser(loggedInUser.nic, formatDate(startDate), formatDate(endDate))
        /* Initiate Adapter */
        attendanceReportsListAdapter =
            AttendanceDataReportsListAdapter(object :
                AttendanceDataReportsListAdapter.OnItemClickListener {
                override fun itemClick(item: AttendanceData) {
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
            it2?.adapter = attendanceReportsListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Open Method for Date Picker Dialog
     */
    private fun openDatePicker(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val initialDate = if (isStartDate) startDate else endDate

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time

                if (isStartDate) {
                    startDate = selectedDate
                    viewModel.setDate(startDate, endDate)
                } else {
                    endDate = selectedDate
                    viewModel.setDate(startDate, endDate)
                }
                viewModel.getAttendancesByUser(
                    loggedInUser.nic,
                    formatDate(startDate),
                    formatDate(endDate)
                )

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        initialDate.let {
            datePickerDialog.datePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        datePickerDialog.show()
    }
}