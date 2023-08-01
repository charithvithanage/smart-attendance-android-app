package lnbti.charithgtp01.smartattendanceuserapp.ui.scan

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.zxing.Result
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.MEDIA_TYPE_EMPLOYEE_SIGNATURE
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.PERMISSION_ALL
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SCANNER_PERMISSIONS
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityScanBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ImageInspectionListener
import lnbti.charithgtp01.smartattendanceuserapp.ui.sign.EmployeeAuthorizationActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.goToHomeActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.hasPermissions
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateToAnotherActivity
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.File

@AndroidEntryPoint
class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private var binding: ActivityScanBinding? = null
    var contentFrame: ViewGroup? = null
    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
    }


    private fun initView() {
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
                    DialogUtils.showErrorDialog(this@ScanActivity,
                        getString(R.string.no_permission_to_camera),
                        object : DialogButtonClickListener {
                            override fun onButtonClick() {
                            }
                        })
                }
                return
            }
        }
    }


    override fun handleResult(rawResult: Result) {
        stopCamera()
        val barCodeString = rawResult.text
        navigateToAnotherActivity(
            this@ScanActivity,
            EmployeeAuthorizationActivity::class.java
        )
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


}