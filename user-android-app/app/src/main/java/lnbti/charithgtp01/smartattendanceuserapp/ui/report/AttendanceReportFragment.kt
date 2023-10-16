package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialogInFragment
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialogInFragment
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.parseStringToUserList
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
    var userRole: String? = null
    private lateinit var allAttendanceList: List<AttendanceData>
    private lateinit var filteredAttendanceList: List<AttendanceData>

    //Users Dropdown values
    lateinit var spinnerOptions: List<User>

    lateinit var selectedUser: User
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
        userRole = getObjectFromSharedPref(requireContext(), Constants.USER_ROLE)

        allAttendanceList = listOf()
        filteredAttendanceList = listOf()

        /**
         * User filter showing only for back office users
         */
        if (userRole == getString(R.string.employee)) {
            binding?.spinner?.visibility = View.GONE
        }else{
            val usersListString = getObjectFromSharedPref(requireContext(), Constants.USERS_LIST)
            spinnerOptions = parseStringToUserList(usersListString)
            selectedUser = spinnerOptions[0]
            val adapter = UserSpinnerAdapter(requireContext(), spinnerOptions)
            binding?.spinner?.adapter = adapter

            binding?.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedUser = spinnerOptions[position]
                    viewModel.onUserSelected(selectedUser)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle nothing selected if needed
                }
            }
        }

        binding?.fromLayout?.mainLayout?.setOnClickListener {
            openDatePicker(true)
        }

        binding?.toLayout?.mainLayout?.setOnClickListener {
            openDatePicker(false)
        }
    }

    private fun filterList(user: User): List<AttendanceData> {
        val tempList = mutableListOf<AttendanceData>() // Use a mutable list

        for (item in allAttendanceList) {
            if (item.userID == user.nic) {
                tempList.add(item) // Add the matching item to the tempList
            }
        }

        return tempList.toList()
    }

    /**
     * Live Data Updates
     */
    private fun viewModelObservers() {
        /* Show error message in the custom error dialog */
        viewModel.errorMessage.observe(requireActivity()) {
            showErrorDialogInFragment(
                this@AttendanceReportFragment,
                it
            )
        }

        viewModel.isDialogVisible.observe(requireActivity()) {
            if (it) {
                /* Show dialog when calling the API */
                dialog = showProgressDialogInFragment(
                    this@AttendanceReportFragment,
                    getString(R.string.wait)
                )
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
                allAttendanceList = gson.fromJson(apiResult.data, listType)
                if (userRole == getString(R.string.employee)) {
                    filteredAttendanceList = allAttendanceList
                    viewModel.setCount(filteredAttendanceList.size)
                    attendanceReportsListAdapter.submitList(filteredAttendanceList)
                } else {
                    viewModel.onUserSelected(selectedUser)
                }

            } else if (apiResult?.data != null) {
                showErrorDialog(requireActivity(), apiResult.data.toString())
            }
        }

        // Observe the selected user in the ViewModel
        viewModel.selectedUser.observe(requireActivity(), Observer { user ->
            // Handle the selected user
            filteredAttendanceList = filterList(user)
            attendanceReportsListAdapter.submitList(filteredAttendanceList)
            viewModel.setCount(filteredAttendanceList.size)
        })
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

        getDataFromServer()

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

    private fun getDataFromServer() {
        if (userRole == getString(R.string.employee)) {
            viewModel.getAttendancesByUser(
                loggedInUser.nic,
                formatDate(startDate),
                formatDate(endDate)
            )
        } else {
            viewModel.getAttendancesFromAllUsers(
                formatDate(startDate),
                formatDate(endDate)
            )
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
                getDataFromServer()

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