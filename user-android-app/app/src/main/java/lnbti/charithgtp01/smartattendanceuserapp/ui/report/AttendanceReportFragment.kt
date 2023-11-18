package lnbti.charithgtp01.smartattendanceuserapp.ui.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentAttendanceReportBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceData
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.userdetails.UserDetailsActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialogInFragment
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialogInFragment
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
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
    private lateinit var binding: FragmentAttendanceReportBinding
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
        ViewModelProvider(requireActivity())[AttendanceDataReportViewModel::class.java].apply {
            viewModel = this
            FragmentAttendanceReportBinding.inflate(inflater, container, false).apply {
                binding = this
                vm = viewModel
                lifecycleOwner = this@AttendanceReportFragment

                binding.fromLayout.mainLayout.setOnClickListener {
                    openDatePicker(true)
                }

                binding.toLayout.mainLayout.setOnClickListener {
                    openDatePicker(false)
                }
            }
        }

        return binding.root
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

        binding.apply {
            /**
             * User filter showing only for back office users
             */
            when (userRole) {
                getString(R.string.employee) -> spinner.isVisible = false
                else -> {
                    parseStringToUserList(
                        getObjectFromSharedPref(
                            requireContext(),
                            Constants.USERS_LIST
                        )
                    ).apply {
                        spinnerOptions = this
                        selectedUser = this[0]
                        spinner.adapter = UserSpinnerAdapter(requireContext(), this)
                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
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
                }
            }
        }
    }

    private fun filterList(user: User): List<AttendanceData> =
        allAttendanceList.filter { it.userID == user.nic }.toMutableList().apply {
            // Additional operations if needed
            // For example: sort by date
            sortByDescending { it.date }
        }.toList()

    /**
     * Live Data Updates
     */
    private fun viewModelObservers() {
        viewModel.apply {
            /* Show error message in the custom error dialog */
            errorMessage.observe(requireActivity()) {
                showErrorDialogInFragment(
                    this@AttendanceReportFragment,
                    it
                )
            }

            isDialogVisible.observe(requireActivity()) {
                when {
                    it -> dialog = showProgressDialogInFragment(
                        this@AttendanceReportFragment,
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
            responseResult.observe(requireActivity()) {
                it?.let { response ->
                    when {
                        response.success == true -> {
                            val type = object : TypeToken<List<AttendanceData>>() {}.type
                            allAttendanceList = gson.fromJson(response.data, type)

                            when (userRole) {
                                getString(R.string.employee) -> allAttendanceList.run {
                                    filteredAttendanceList = this
                                    setCount(this.size)
                                    attendanceReportsListAdapter.submitList(this)
                                }

                                else -> onUserSelected(this@AttendanceReportFragment.selectedUser)
                            }
                        }

                        response.data != null ->
                            showErrorDialog(requireActivity(), response.data.toString())
                    }
                }
            }

            // Observe the selected user in the ViewModel
            selectedUser.observe(requireActivity()) { user ->
                filterList(user).apply {
                    // Handle the selected user
                    filteredAttendanceList = this
                    attendanceReportsListAdapter.submitList(this)
                    setCount(this.size)
                }
            }
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

        getDataFromServer()

        /* Initiate Adapter */
        attendanceReportsListAdapter =
            AttendanceDataReportsListAdapter(object :
                AttendanceDataReportsListAdapter.OnItemClickListener {
                override fun itemClick(item: AttendanceData) {
                    HashMap<String, String>().apply {
                        this[Constants.OBJECT_STRING] = gson.toJson(item)
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
            it2.adapter = attendanceReportsListAdapter
        }
    }

    private fun getDataFromServer() {
        if (NetworkUtils.isNetworkAvailable()) {
            when (userRole) {
                getString(R.string.employee) -> viewModel.getAttendancesByUser(
                    loggedInUser.nic,
                    formatDate(startDate),
                    formatDate(endDate)
                )

                else -> viewModel.getAttendancesFromAllUsers(
                    formatDate(startDate),
                    formatDate(endDate)
                )
            }
        } else {
            viewModel.setErrorMessage(getString(R.string.no_internet))
        }

    }

    /**
     * Open Method for Date Picker Dialog
     */
    private fun openDatePicker(isStartDate: Boolean) {
        val initialDate = when {
            isStartDate -> startDate
            else -> endDate
        }

        Calendar.getInstance().apply {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    set(year, month, dayOfMonth)
                    val selectedDate = time

                    when {
                        isStartDate -> {
                            startDate = selectedDate
                            viewModel.setDate(startDate, endDate)
                        }

                        else -> {
                            endDate = selectedDate
                            viewModel.setDate(startDate, endDate)
                        }
                    }
                    getDataFromServer()

                },
                get(Calendar.YEAR),
                get(Calendar.MONTH),
                get(Calendar.DAY_OF_MONTH)
            )

            initialDate.let {
                datePickerDialog.datePicker.updateDate(
                    get(Calendar.YEAR),
                    get(Calendar.MONTH),
                    get(Calendar.DAY_OF_MONTH)
                )
            }
            datePickerDialog.show()
        }
    }
}