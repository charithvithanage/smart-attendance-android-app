package lnbti.charithgtp01.smartattendanceuserapp.ui.sign

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.customviews.SignatureView
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityEmployeeAuthorizationBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceData
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkInRequest
import lnbti.charithgtp01.smartattendanceuserapp.model.AttendanceMarkOutRequest
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.FileUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.NetworkUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatTime
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.formatTodayDate
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.goToHomeActivity
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

@AndroidEntryPoint
class EmployeeAuthorizationActivity : AppCompatActivity() {
    private var binding: ActivityEmployeeAuthorizationBinding? = null
    private var drawingView: SignatureView? = null
    private var mediaFile: File? = null
    lateinit var nic: String
    private lateinit var employeeAuthorizationViewModel: EmployeeAuthorizationViewModel
    private var dialog: DialogFragment? = null
    private var attendanceType: String? = null
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        viewModelObservers()
        setData()
    }

    private fun setData() {
        //Get Today Attendance and select In and Out type
        employeeAuthorizationViewModel.getTodayAttendanceByUser(
            nic,
            formatTodayDate(this@EmployeeAuthorizationActivity)
        )
    }

    private fun initView() {
        val nicString = intent.getStringExtra(Constants.OBJECT_STRING)
        if (nicString != null)
            nic = nicString
        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val gestureOverlayView = findViewById<RelativeLayout>(R.id.drawing_pad)
        val buttonConfirm: ImageButton = findViewById(R.id.btnConfirm)
        mediaFile = outputMediaFileUri
        drawingView =
            SignatureView(this@EmployeeAuthorizationActivity, null, gestureOverlayView, mainLayout)
        gestureOverlayView.addView(drawingView)
        val display = this@EmployeeAuthorizationActivity.resources.displayMetrics
        val params = gestureOverlayView.layoutParams
        params.height = (display.heightPixels - UIUtils.convertDpToPixel(
            196f,
            this@EmployeeAuthorizationActivity
        )).toInt()
        params.width = (display.widthPixels - UIUtils.convertDpToPixel(
            100f,
            this@EmployeeAuthorizationActivity
        )).toInt()
        gestureOverlayView.layoutParams = params
        buttonConfirm.setOnClickListener { v: View? ->
            if (NetworkUtils.isNetworkAvailable()) {
                if (drawingView!!.isDraw) {
                    saveDrawing()
                } else {
                    showErrorDialog(
                        this@EmployeeAuthorizationActivity,
                        getString(R.string.no_signature_found)
                    )
                }
            } else {
                showErrorDialog(this@EmployeeAuthorizationActivity, getString(R.string.no_internet))
            }

        }
        findViewById<View>(R.id.btnRemove).setOnClickListener { v: View? ->
            onBackPressed()
        }
        findViewById<View>(R.id.btnClear).setOnClickListener { v: View? -> drawingView!!.onClickClear() }

    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_authorization)
        //Data binding
        employeeAuthorizationViewModel =
            ViewModelProvider(this)[EmployeeAuthorizationViewModel::class.java]
        binding?.vm = employeeAuthorizationViewModel
        binding?.lifecycleOwner = this
    }

    /**
     * Create a file Uri for saving an image or video
     *
     * @return
     */
    private val outputMediaFileUri: File?
        private get() = FileUtils.getOutputMediaFile(
            this,
            nic, Constants.MEDIA_TYPE_EMPLOYEE_SIGNATURE
        )

    private fun saveDrawing() {
        try {
            val workingBitmap = drawingView!!.bitmap
            val fileOutputStream = FileOutputStream(mediaFile)

            // Compress bitmap to png image.
            workingBitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream)

            // Flush bitmap to image file.
            fileOutputStream.flush()

            // Close the output stream.
            fileOutputStream.close()

            val calendar = Calendar.getInstance()
            val formattedDate = formatDate(calendar.time)
            val formattedTime = formatTime(calendar.time)

            if (attendanceType == "in") {
                val attendanceMarkInRequest = AttendanceMarkInRequest(
                    userID = nic,
                    date = formattedDate,
                    inTime = formattedTime
                )

                Log.d(Constants.TAG, "Attendance Type: In " + gson.toJson(attendanceMarkInRequest))
                employeeAuthorizationViewModel.markIn(
                    attendanceMarkInRequest
                )
            } else {
                val attendanceMarkOutRequest = AttendanceMarkOutRequest(
                    userID = nic,
                    date = formattedDate,
                    outTime = formattedTime
                )

                employeeAuthorizationViewModel.markOut(
                    attendanceMarkOutRequest
                )
            }
        } catch (e: Exception) {
            Log.v("Signature Gestures", e.toString())
            e.printStackTrace()
        }
    }

    private fun viewModelObservers() {

        /* Show error message in the custom error dialog */
        employeeAuthorizationViewModel.errorMessage.observe(this@EmployeeAuthorizationActivity) {
            showAlertDialog(
                this@EmployeeAuthorizationActivity,
                Constants.FAIL,
                it,
                object : CustomAlertDialogListener {
                    override fun onDialogButtonClicked() {
                    }
                })
        }

        employeeAuthorizationViewModel.isDialogVisible.observe(this@EmployeeAuthorizationActivity) {
            if (it) {
                /* Show dialog when calling the API */
                dialog = DialogUtils.showProgressDialog(
                    this@EmployeeAuthorizationActivity,
                    getString(R.string.wait)
                )
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }

        //Waiting for Api response
        employeeAuthorizationViewModel.attendanceMarkResult.observe(this@EmployeeAuthorizationActivity) {
            val apiResult = it
            if (apiResult?.success == true) {
                showAlertDialog(
                    this, Constants.SUCCESS,
                    apiResult.message,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            goToHomeActivity(this@EmployeeAuthorizationActivity)

                        }

                    })
            } else {
                showAlertDialog(
                    this, Constants.FAIL,
                    apiResult?.message,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                        }
                    })
            }
        }
        //Waiting for Today Attendance response
        employeeAuthorizationViewModel.attendanceResult.observe(this@EmployeeAuthorizationActivity) {
            val apiResult = it
            if (apiResult?.success == true) {
                val attendanceData = gson.fromJson(apiResult.data, AttendanceData::class.java)
                if (attendanceData.inTime != null && attendanceData.outTime == null) {
                    attendanceType = "out"
                } else {
                    showAlertDialog(
                        this, Constants.FAIL,
                        getString(R.string.attendancealreadymarked),
                        object : CustomAlertDialogListener {
                            override fun onDialogButtonClicked() {
                                goToHomeActivity(this@EmployeeAuthorizationActivity)
                            }
                        })
                }
            } else {
                attendanceType = "in"
            }
        }
    }

}