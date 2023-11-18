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
import androidx.core.view.isVisible
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
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatTodayDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getCurrentLocation
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.saveObjectInSharedPref
import java.util.concurrent.Executor

/**
 * A [Fragment] representing the home screen of the application. This fragment
 * handles user interface logic, including displaying attendance information
 * for employees and managing a list of users for business users.
 *
 * The [HomeFragment] uses View Binding to interact with the UI components and
 * communicates with a [HomeViewModel] to fetch and update data.
 *
 * @constructor Creates a new instance of [HomeFragment].
 *
 * @property binding View Binding for the Fragment
 * @property viewModel ViewModel for managing data and business logic
 * @property usersListAdapter Adapter for displaying the list of users
 * @property dialog Dialog for displaying loading or error messages
 * @property executor Executor for handling asynchronous tasks
 * @property biometricPrompt BiometricPrompt for fingerprint authentication
 * @property promptInfo PromptInfo for configuring the BiometricPrompt
 * @property mFusedLocationClient FusedLocationProviderClient for obtaining the device's location
 * @property gson Gson instance for JSON serialization and deserialization
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
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
        ViewModelProvider(requireActivity())[HomeViewModel::class.java].apply {
            viewModel = this
            binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
                vm = viewModel
                lifecycleOwner = this@HomeFragment
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //If the logged in user's user role is Business User should show Attendance mark users list page in the home fragment
        //Else need to show fab button to scan the qr
        val userRole = getObjectFromSharedPref(requireContext(), Constants.USER_ROLE)
        val loggedInUserString = getObjectFromSharedPref(requireContext(), Constants.LOGGED_IN_USER)
        viewModel.apply {
            binding.apply {
                when (userRole) {
                    getString(R.string.employee) -> {
                        // Logic for displaying UI elements for employees
                        userLayout.isVisible = true
                        businessUserLayout.isVisible = false
                        gson.fromJson(loggedInUserString, User::class.java).apply {
                            name = "$firstName $lastName"

                            btnMarkIn.setOnClickListener {
                                onClickScan("in")
                            }

                            btnMarkOut.setOnClickListener {
                                onClickScan("out")
                            }

                            getTodayAttendanceByUser(nic, formatTodayDate(requireContext()))
                        }
                    }

                    else -> {
                        // Logic for displaying UI elements for business users
                        userLayout.isVisible = false
                        businessUserLayout.isVisible = true
                        if (NetworkUtils.isNetworkAvailable()) {
                            getUsersList()
                        } else {
                            viewModel.setErrorMessage(getString(R.string.no_internet))
                        }
                    }
                }
            }
        }
        // Initialize the RecyclerView adapter
        initiateAdapter()

        // Set up observers for LiveData updates
        viewModelObservers()

    }

    /**
     * Observes changes in ViewModel's LiveData and updates the UI accordingly.
     */
    private fun viewModelObservers() {
        viewModel.apply {
            // Observe error messages and display them in a dialog
            errorMessage.observe(requireActivity()) {
                DialogUtils.showErrorDialogInFragment(
                    this@HomeFragment,
                    it
                )
            }

            // Observe loading state and show/hide a progress dialog
            isDialogVisible.observe(requireActivity()) {
                if (it) {
                    // Show dialog when calling the API
                    if (dialog?.isVisible == false)
                        dialog =
                            showProgressDialogInFragment(
                                this@HomeFragment,
                                getString(R.string.wait)
                            )
                } else {
                    // Dismiss dialog after updating the data list
                    dialog?.dismiss()
                }
            }

            // Observe the list of users and update the RecyclerView
            usersList.observe(requireActivity()) {
                // Save users list locally for later use
                saveObjectInSharedPref(
                    requireActivity(),
                    USERS_LIST,
                    gson.toJson(it),
                    SuccessListener { usersListAdapter.submitList(it) })
            }

            // Observe API response for attendance and update UI
            attendanceResult.observe(requireActivity()) {
                binding.apply {
                    val apiResult = it
                    when (apiResult?.success) {
                        // Logic for handling attendance data
                        true -> gson.fromJson(apiResult.data, AttendanceData::class.java).run {
                            setAttendanceData(this)
                            btnMarkIn.isVisible = false

                            when (outTime) {
                                else -> btnMarkOut.isVisible = false
                            }
                        }

                        else -> {
                            btnMarkIn.isVisible = true
                            btnMarkOut.isVisible = false
                        }
                    }
                }

            }
        }

    }

    /**
     * Configures and initializes the RecyclerView adapter.
     */
    private fun initiateAdapter() {
        /* Initiate Adapter */
        usersListAdapter =
            HomeListAdapter(object : HomeListAdapter.OnItemClickListener {
                override fun scan(selectedUser: User) {
                    // Logic for handling user scan action
                    qrHandshake(object : QRHandshakeListener {
                        override fun onSuccess(location: Location) {
                            HashMap<String, String>().apply {
                                selectedUser.lat = location.latitude
                                selectedUser.long = location.longitude
                                this[Constants.OBJECT_STRING] = gson.toJson(selectedUser)
                                navigateToAnotherActivityWithExtras(
                                    requireActivity(),
                                    ScanActivity::class.java,
                                    this
                                )
                            }
                        }

                        override fun onError(error: String) {
                            TODO("Not yet implemented")
                        }
                    })


                }

                override fun generate(item: User) {
                    // Logic for handling QR code generation action
                    qrHandshake(object : QRHandshakeListener {
                        override fun onSuccess(location: Location) {
                            HashMap<String, String>().apply {
                                item.lat = location.latitude
                                item.long = location.longitude
                                val encryptedValue = encrypt(SECURE_KEY, gson.toJson(item))
                                this[Constants.OBJECT_STRING] = encryptedValue as String

                                navigateToAnotherActivityWithExtras(
                                    requireActivity(),
                                    AttendanceQRActivity::class.java,
                                    this
                                )
                            }
                        }

                        override fun onError(error: String) {
                            Log.d(Constants.TAG, error)
                        }
                    })
                }
            })

        // Set the adapter to the RecyclerView
        binding.recyclerView.also { it2 ->
            it2?.adapter = usersListAdapter
        }
    }

    /**
     * Initiates the QR handshake process for obtaining location information.
     *
     * @param qrHandshakeListener Listener for QR handshake events.
     */
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
                            gson.fromJson(
                                getObjectFromSharedPref(
                                    requireContext(),
                                    Constants.LOGGED_IN_USER
                                ), User::class.java
                            ).apply {
                                lat = location.latitude
                                long = location.longitude
                                HashMap<String, String>().apply {
                                    this[Constants.OBJECT_STRING] = gson.toJson(this)
                                    this[Constants.ATTENDANCE_TYPE] = attendanceType
                                    navigateToAnotherActivityWithExtras(
                                        requireActivity(),
                                        ScanActivity::class.java,
                                        this
                                    )
                                }
                            }
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