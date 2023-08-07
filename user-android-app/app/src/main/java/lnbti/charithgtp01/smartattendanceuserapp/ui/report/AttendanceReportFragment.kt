package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.app.DatePickerDialog
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
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentAttendanceReportBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceDate
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceStatus
import lnbti.charithgtp01.smartattendanceuserapp.model.CalendarDate
import lnbti.charithgtp01.smartattendanceuserapp.ui.userdetails.UserDetailsActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import java.util.Calendar
import java.util.Date

/**
 * Attendance Report Fragment
 */
@AndroidEntryPoint
class AttendanceReportFragment : Fragment() {
    private var binding: FragmentAttendanceReportBinding? = null
    private lateinit var viewModel: AttendanceReportViewModel
    private lateinit var attendanceReportsListAdapter: AttendanceReportsListAdapter
    private var dialog: Dialog? = null

    //Initially Start Date Set to Today and End Date Set to Last Day of the current month
    private var startDate: Date = Date()
    private var endDate: Date = Utils.getLastDayOfMonth(startDate)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*
         * Initiate Data Binding and View Model
        */
        binding = FragmentAttendanceReportBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AttendanceReportViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initiateAdapter()
        initiateProgressDialog()
        viewModelObservers()


    }

    private fun initView() {
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
            DialogUtils.showErrorDialog(
                requireContext(),
                it)
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
        viewModel.calendarDates.observe(requireActivity()) {
            attendanceReportsListAdapter.submitList(it)
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
        attendanceReportsListAdapter =
            AttendanceReportsListAdapter(object : AttendanceReportsListAdapter.OnItemClickListener {
                override fun itemClick(item: AttendanceDate) {
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
                    viewModel.setDate(startDate,endDate)
                } else {
                    endDate = selectedDate
                    viewModel.setDate(startDate,endDate)

                }
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