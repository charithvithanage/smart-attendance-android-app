package lnbti.charithgtp01.smartattendanceuserapp.ui.home

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.Keystore.Companion.encrypt
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SECURE_KEY
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.USERS_LIST
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentHomeBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.GetCurrentLocationListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.QRHandshakeListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.SuccessListener
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceData
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.qr.attendance.AttendanceQRActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.scan.ScanActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialogInFragment
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatTodayDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getCurrentLocation
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.saveObjectInSharedPref
import java.util.concurrent.Executor

/**
 * Users Fragment
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var usersListAdapter: HomeListAdapter
    private var dialog: DialogFragment? = null
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    val gson = Gson()
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

        //If the logged in user's user role is Business User should show Attendance mark users list page in the home fragment
        //Else need to show fab button to scan the qr
        val userRole = getObjectFromSharedPref(requireContext(), Constants.USER_ROLE)
        val loggedInUserString = getObjectFromSharedPref(requireContext(), Constants.LOGGED_IN_USER)
        val gson = Gson()
        val loggedInUser: User = gson.fromJson(loggedInUserString, User::class.java)
        if (userRole == getString(R.string.employee)) {
            binding?.userLayout?.visibility = View.VISIBLE
            binding?.businessUserLayout?.visibility = View.GONE
            viewModel.name = loggedInUser.firstName + " " + loggedInUser.lastName

            binding?.btnMarkIn?.setOnClickListener {
                onClickScan("in")
            }

            binding?.btnMarkOut?.setOnClickListener {
                onClickScan("out")
            }

            viewModel.getTodayAttendanceByUser(loggedInUser.nic, formatTodayDate(requireContext()))

        } else {
            binding?.userLayout?.visibility = View.GONE
            binding?.businessUserLayout?.visibility = View.VISIBLE
            viewModel.getUsersList()
        }

        initiateAdapter()
        viewModelObservers()

    }

    /**
     * Live Data Updates
     */
    private fun viewModelObservers() {
        /* Show error message in the custom error dialog */
        viewModel.errorMessage.observe(requireActivity()) {
            DialogUtils.showErrorDialogInFragment(
                this@HomeFragment,
                it
            )
        }

        viewModel.isDialogVisible.observe(requireActivity()) {
            if (it) {
                /* Show dialog when calling the API */
                if (dialog?.isVisible == false)
                    dialog =
                        showProgressDialogInFragment(this@HomeFragment, getString(R.string.wait))
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }

        /* Observer to catch list data
        * Update Recycle View Items using Diff Utils
        */
        viewModel.usersList.observe(requireActivity()) {
            //Save users list locally to use in reports
            saveObjectInSharedPref(
                requireActivity(),
                USERS_LIST,
                gson.toJson(it),
                SuccessListener { usersListAdapter.submitList(it) })
        }

        //Waiting for Api response
        viewModel.attendanceResult.observe(requireActivity()) {
            val apiResult = it
            if (apiResult?.success == true) {

                val attendanceData = gson.fromJson(apiResult.data, AttendanceData::class.java)
                viewModel.setAttendanceData(attendanceData)
                binding?.btnMarkIn?.visibility = View.GONE
                if (attendanceData.inTime != null && attendanceData.outTime == null) {
                    binding?.btnMarkOut?.visibility = View.VISIBLE
                } else {
                    binding?.btnMarkOut?.visibility = View.GONE

                }
            } else {
                binding?.btnMarkIn?.visibility = View.VISIBLE
                binding?.btnMarkOut?.visibility = View.GONE
            }
        }
    }

    /**
     * Recycle View data configuration
     */
    private fun initiateAdapter() {
        /* Initiate Adapter */
        usersListAdapter =
            HomeListAdapter(object : HomeListAdapter.OnItemClickListener {
                override fun scan(selectedUser: User) {

                    qrHandshake(object : QRHandshakeListener {
                        override fun onSuccess(location: Location) {
                            val gson = Gson()
                            val prefMap = HashMap<String, String>()
                            selectedUser.lat = location.latitude
                            selectedUser.long = location.longitude
                            prefMap[Constants.OBJECT_STRING] = gson.toJson(selectedUser)
                            navigateToAnotherActivityWithExtras(
                                requireActivity(),
                                ScanActivity::class.java,
                                prefMap
                            )
                        }

                        override fun onError(error: String) {
                            TODO("Not yet implemented")
                        }
                    })


                }

                override fun generate(item: User) {
                    qrHandshake(object : QRHandshakeListener {
                        override fun onSuccess(location: Location) {
                            val gson = Gson()
                            val prefMap = HashMap<String, String>()
                            item.lat = location.latitude
                            item.long = location.longitude
                            val encryptedValue = encrypt(SECURE_KEY, gson.toJson(item))
                            prefMap[Constants.OBJECT_STRING] = encryptedValue as String

                            navigateToAnotherActivityWithExtras(
                                requireActivity(),
                                AttendanceQRActivity::class.java,
                                prefMap
                            )
                        }

                        override fun onError(error: String) {
                            Log.d(Constants.TAG, error)
                        }
                    })
                }
            })

        /* Set Adapter to Recycle View */
        binding?.recyclerView.also { it2 ->
            it2?.adapter = usersListAdapter
        }
    }

    private fun qrHandshake(qrHandshakeListener: QRHandshakeListener) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocation(requireActivity(), mFusedLocationClient, object :
            GetCurrentLocationListener {
            override fun onLocationRead(location: Location) {
                qrHandshakeListener.onSuccess(location)
            }

            override fun requestPermission() {
                TODO("Not yet implemented")
            }

            override fun onError(error: String) {
                TODO("Not yet implemented")
            }

            override fun openSettings() {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Show finger print authentication dialog
     * Employee can scan QR only if the user successfully authenticated
     */
    private fun onClickScan(attendanceType: String) {

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    qrHandshake(object : QRHandshakeListener {
                        override fun onSuccess(location: Location) {
                            val gson = Gson()
                            val prefMap = HashMap<String, String>()
                            val loggedInUserString =
                                getObjectFromSharedPref(requireContext(), Constants.LOGGED_IN_USER)
                            val loggedInUser = gson.fromJson(loggedInUserString, User::class.java)
                            loggedInUser.lat = location.latitude
                            loggedInUser.long = location.longitude
                            prefMap[Constants.OBJECT_STRING] = gson.toJson(loggedInUser)
                            prefMap[Constants.ATTENDANCE_TYPE] = attendanceType
                            navigateToAnotherActivityWithExtras(
                                requireActivity(),
                                ScanActivity::class.java,
                                prefMap
                            )
                        }

                        override fun onError(error: String) {
                            TODO("Not yet implemented")
                        }
                    })
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(), "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setAllowedAuthenticators(BIOMETRIC_WEAK or BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .setConfirmationRequired(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

}