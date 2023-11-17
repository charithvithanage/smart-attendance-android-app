package lnbti.charithgtp01.smartattendanceuserapp.ui.scan

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.zxing.Result
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.Keystore
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.ATTENDANCE_TYPE
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.FAIL
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.PERMISSION_ALL
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SCANNER_PERMISSIONS
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SECURE_KEY
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SUCCESS
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.USER_ROLE
import lnbti.charithgtp01.smartattendanceuserapp.constants.ResourceConstants.DECRYPTION_ERROR
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityScanBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.ui.sign.EmployeeAuthorizationActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.goToHomeActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.hasPermissions
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.text.SimpleDateFormat
import java.util.Calendar.getInstance
import java.util.Locale

@AndroidEntryPoint
class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var binding: ActivityScanBinding? = null
    var contentFrame: ViewGroup? = null
    private var mScannerView: ZXingScannerView? = null
    private lateinit var loggedInUser: User
    val gson = Gson()
    private lateinit var scanViewModel: ScanViewModel
    private var dialog: DialogFragment? = null
    private var attendanceType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        viewModelObservers()
    }


    private fun initView() {
        attendanceType = intent.getStringExtra(ATTENDANCE_TYPE)
        contentFrame = findViewById(R.id.content_frame)

        initiateActionBar(
            binding?.actionBar?.mainLayout!!,
            getString(R.string.scan_qr),
            object : ActionBarListener {
                override fun backPressed() {
                    onBackPressed()
                }

                override fun homePressed() {
                    goToHomeActivity(this@ScanActivity)
                }
            })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(this@ScanActivity, SCANNER_PERMISSIONS)) {
                ActivityCompat.requestPermissions(
                    this@ScanActivity,
                    SCANNER_PERMISSIONS,
                    PERMISSION_ALL
                )
            } else {
                openScanner()
            }
        } else {
            openScanner()
        }
    }

    private fun openScanner() {
        mScannerView = ZXingScannerView(this)
        contentFrame!!.addView(mScannerView)
        (mScannerView as ZXingScannerView).setLaserEnabled(false)
    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan)
        //Data binding
        scanViewModel = ViewModelProvider(this)[ScanViewModel::class.java]
        binding?.vm = scanViewModel
        binding?.lifecycleOwner = this
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_ALL -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openScanner()
                } else {
                    showErrorDialog(
                        this@ScanActivity,
                        getString(R.string.no_permission_to_camera)
                    )
                }
                return
            }
        }
    }


    override fun handleResult(rawResult: Result) {
        stopCamera()
        val barCodeString = rawResult.text

        val userRole = Utils.getObjectFromSharedPref(this, USER_ROLE)

        if (userRole == getString(R.string.employee)) {
            val loggedInUserString = intent.getStringExtra(OBJECT_STRING)
            loggedInUser = gson.fromJson(loggedInUserString, User::class.java)

            try {
                val selectedUserString = Keystore.decrypt(SECURE_KEY, barCodeString)

                var scannedUser = gson.fromJson(selectedUserString, User::class.java)

                val isValidLocation = Utils.isLocationCorrect(
                    scannedUser.lat,
                    scannedUser.long,
                    loggedInUser.lat,
                    loggedInUser.long
                )

                if (isValidLocation) {
                    if (scannedUser.nic == loggedInUser.nic) {
                        val calendar = getInstance()
                        val dateFormat =
                            SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
                        val formattedDate = dateFormat.format(calendar.time)

                        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
                        val formattedTime = timeFormat.format(calendar.time)

                        if (attendanceType == "in") {
                            val attendanceMarkInRequest = AttendanceMarkInRequest(
                                userID = loggedInUser.nic,
                                date = formattedDate,
                                inTime = formattedTime
                            )

                            scanViewModel.markIn(
                                attendanceMarkInRequest
                            )
                        } else {
                            val attendanceMarkOutRequest = AttendanceMarkOutRequest(
                                userID = loggedInUser.nic,
                                date = formattedDate,
                                outTime = formattedTime
                            )

                            scanViewModel.markOut(
                                attendanceMarkOutRequest
                            )
                        }
                    } else {
                        showAlertDialog(
                            this@ScanActivity,
                            FAIL,
                            "Invalid User",
                            object : CustomAlertDialogListener {
                                override fun onDialogButtonClicked() {
                                    startCamera()
                                }
                            })
                    }
                } else {
                    showAlertDialog(
                        this@ScanActivity,
                        FAIL,
                        "Invalid Location",
                        object : CustomAlertDialogListener {
                            override fun onDialogButtonClicked() {
                                startCamera()
                            }
                        })
                }
            } catch (e: Exception) {
                showAlertDialog(
                    this@ScanActivity,
                    FAIL,
                    "Invalid QR. Scan Correct QR",
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            startCamera()
                        }
                    })
            }
        } else {

            try {
                val decryptedValue = Keystore.decrypt(SECURE_KEY, barCodeString)
                val selectedUserString = intent.getStringExtra(OBJECT_STRING)
                val selectedUser = gson.fromJson(selectedUserString, User::class.java)

                if (decryptedValue == selectedUser.nic) {
                    val prefMap = HashMap<String, String>()
                    prefMap[OBJECT_STRING] = decryptedValue
                    navigateToAnotherActivityWithExtras(
                        this@ScanActivity,
                        EmployeeAuthorizationActivity::class.java, prefMap
                    )
                } else {
                    showAlertDialog(
                        this@ScanActivity,
                        FAIL,
                        "Invalid User",
                        object : CustomAlertDialogListener {
                            override fun onDialogButtonClicked() {
                                startCamera()
                            }
                        })
                }
            } catch (e: Exception) {
                showAlertDialog(
                    this@ScanActivity,
                    FAIL,
                    DECRYPTION_ERROR,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            startCamera()
                        }
                    })
            }


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
        contentFrame!!.visibility = View.VISIBLE
        if (mScannerView != null) {
            mScannerView!!.setResultHandler(this)
            mScannerView!!.startCamera()
        }
    }

    private fun stopCamera() {
        contentFrame!!.visibility = View.GONE
        if (mScannerView != null) {
            mScannerView!!.stopCamera()
        }
    }

    private fun viewModelObservers() {

        /* Show error message in the custom error dialog */
        scanViewModel.errorMessage.observe(this@ScanActivity) {
            showAlertDialog(
                this@ScanActivity,
                FAIL,
                it,
                object : CustomAlertDialogListener {
                    override fun onDialogButtonClicked() {
                        startCamera()
                    }
                })
        }

        scanViewModel.isDialogVisible.observe(this@ScanActivity) {
            if (it) {
                /* Show dialog when calling the API */
                dialog = showProgressDialog(this@ScanActivity, getString(R.string.wait))
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }

        //Waiting for Api response
        scanViewModel.attendanceMarkResult.observe(this@ScanActivity) {
            val apiResult = it
            if (apiResult?.success == true) {
                showAlertDialog(
                    this, SUCCESS,
                    apiResult.message,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            goToHomeActivity(this@ScanActivity)

                        }

                    })
            } else {
                showAlertDialog(
                    this, Constants.FAIL,
                    apiResult?.message,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            startCamera()
                        }
                    })
            }
        }
    }
}