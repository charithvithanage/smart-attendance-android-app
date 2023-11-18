package lnbti.charithgtp01.smartattendanceuserapp.ui.scan

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.zxing.Result
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.other.Keystore
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.FAIL
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.PERMISSION_ALL
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SCANNER_PERMISSIONS
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SECURE_KEY
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SUCCESS
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.USER_ROLE
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityScanBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.sign.EmployeeAuthorizationActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatTime
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.goToHomeActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.hasPermissions
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.Calendar.getInstance

@AndroidEntryPoint
class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var binding: ActivityScanBinding? = null
    private var contentFrame: ViewGroup? = null
    private var mScannerView: ZXingScannerView? = null
    val gson = Gson()
    private lateinit var scanViewModel: ScanViewModel
    private var dialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        viewModelObservers()
    }

    private fun initView() {
        contentFrame = findViewById(R.id.content_frame)

        initiateActionBar(binding?.actionBar?.mainLayout!!,
            getString(R.string.scan_qr),
            object : ActionBarListener {
                override fun backPressed() {
                    onBackPressed()
                }

                override fun homePressed() {
                    goToHomeActivity(this@ScanActivity)
                }
            })

        when {
            !hasPermissions(
                this@ScanActivity,
                SCANNER_PERMISSIONS
            ) -> ActivityCompat.requestPermissions(
                this@ScanActivity, SCANNER_PERMISSIONS, PERMISSION_ALL
            )

            else -> openScanner()
        }
    }

    private fun openScanner() {
        mScannerView = ZXingScannerView(this).apply {
            contentFrame?.addView(this)
            setLaserEnabled(false)
        }
    }

    private fun initiateDataBinding() {
        //Data binding
        ViewModelProvider(this)[ScanViewModel::class.java].apply {
            scanViewModel = this
            DataBindingUtil.setContentView<ActivityScanBinding?>(
                this@ScanActivity,
                R.layout.activity_scan
            )
                .apply {
                    binding = this
                    vm = scanViewModel
                    lifecycleOwner = this@ScanActivity
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_ALL -> {
                when {
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED -> openScanner()
                    else -> showErrorDialog(
                        this@ScanActivity, getString(R.string.no_permission_to_camera)
                    )
                }
                return
            }
        }
    }

    override fun handleResult(rawResult: Result) {
        stopCamera()

        val barCodeString = rawResult.text

        when (getObjectFromSharedPref(this, USER_ROLE)) {
            getString(R.string.employee) -> {
                handleEmployeeRole(barCodeString)
            }
            else -> {
                handleOtherRoles(barCodeString)
            }
        }
    }

    private fun handleEmployeeRole(barCodeString: String) {
        if (NetworkUtils.isNetworkAvailable()) {
            val loggedInUserString = intent.getStringExtra(OBJECT_STRING)
            val loggedInUser = gson.fromJson(loggedInUserString, User::class.java)

            try {
                val selectedUser = Keystore.decrypt(SECURE_KEY, barCodeString).run {
                    gson.fromJson(this, User::class.java)
                }

                val isValidLocation = Utils.isLocationCorrect(
                    selectedUser.lat,
                    selectedUser.long,
                    loggedInUser.lat,
                    loggedInUser.long
                )

                when {
                    isValidLocation -> {
                        when (selectedUser.nic) {
                            loggedInUser.nic -> {
                                val calendar = getInstance()
                                val formattedDate = formatDate(calendar.time)
                                val formattedTime = formatTime(calendar.time)
                                scanViewModel.apply {
                                    when {
                                        intent.getStringExtra(Constants.ATTENDANCE_TYPE) == "in" -> {
                                            markIn(
                                                AttendanceMarkInRequest(
                                                    date = formattedDate
                                                )
                                            )
                                        }

                                        else -> {
                                            markOut(
                                                AttendanceMarkOutRequest(
                                                    userID = loggedInUser.nic,
                                                    date = formattedDate,
                                                    outTime = formattedTime
                                                )
                                            )
                                        }
                                    }
                                }

                            }

                            else -> scanViewModel.setErrorMessage("Invalid User")
                        }
                    }

                    else -> {
                        scanViewModel.setErrorMessage("Invalid Location")
                    }
                }
            } catch (e: Exception) {
                scanViewModel.setErrorMessage("Invalid QR. Scan Correct QR")
            }
        } else {
            scanViewModel.setErrorMessage(getString(R.string.no_internet))
        }

    }

    private fun handleOtherRoles(barCodeString: String) {
        try {
            val decryptedValue = Keystore.decrypt(SECURE_KEY, barCodeString)
            val selectedUserString = intent.getStringExtra(OBJECT_STRING)
            val selectedUser = gson.fromJson(selectedUserString, User::class.java)

            when (decryptedValue) {
                selectedUser.nic -> {
                    val prefMap = hashMapOf(OBJECT_STRING to decryptedValue)
                    navigateToAnotherActivityWithExtras(
                        this@ScanActivity,
                        EmployeeAuthorizationActivity::class.java,
                        prefMap
                    )
                }

                else ->   scanViewModel.setErrorMessage("Invalid User")
            }
        } catch (e: Exception) {
            scanViewModel.setErrorMessage(getString(R.string.decrypt_error))
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        stopCamera()
    }

    private fun startCamera() {
        contentFrame?.apply {
            isVisible = true
        }

        mScannerView?.run {
            setResultHandler(this@ScanActivity) // Assuming 'this' is an instance of YourClass implementing ZXingScannerView.ResultHandler
            startCamera()
        }
    }

    private fun stopCamera() {
        contentFrame?.apply {
            isVisible = false
        }

        mScannerView?.stopCamera()
    }

    private fun viewModelObservers() {

        scanViewModel.apply {
            /* Show error message in the custom error dialog */
            errorMessage.observe(this@ScanActivity) {
                showAlertDialog(this@ScanActivity, FAIL, it, object : CustomAlertDialogListener {
                    override fun onDialogButtonClicked() {
                        startCamera()
                    }
                })
            }

            isDialogVisible.observe(this@ScanActivity) {
                when {
                    it -> dialog = showProgressDialog(this@ScanActivity, getString(R.string.wait))
                    else -> dialog?.dismiss()
                }
                /* Show dialog when calling the API */
                /* Dismiss dialog after updating the data list to recycle view */
            }

            //Waiting for Api response
            attendanceMarkResult.observe(this@ScanActivity) {
                it?.run {
                    when (it.success) {
                        true -> showAlertDialog(this@ScanActivity,
                            SUCCESS,
                            it.message,
                            object : CustomAlertDialogListener {
                                override fun onDialogButtonClicked() {
                                    goToHomeActivity(this@ScanActivity)

                                }

                            })

                        else -> showAlertDialog(this@ScanActivity,
                            FAIL,
                            it.message,
                            object : CustomAlertDialogListener {
                                override fun onDialogButtonClicked() {
                                    startCamera()
                                }
                            })
                    }
                }
            }
        }
    }
}