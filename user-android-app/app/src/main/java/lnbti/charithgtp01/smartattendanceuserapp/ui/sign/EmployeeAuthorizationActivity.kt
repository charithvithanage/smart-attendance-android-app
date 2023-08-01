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
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.customviews.SignatureView
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityEmployeeAuthorizationBinding
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityScanBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.DialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.FileUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.goToHomeActivity
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class EmployeeAuthorizationActivity : AppCompatActivity() {
    private var binding: ActivityEmployeeAuthorizationBinding? = null
    var drawingView: SignatureView? = null
    var mediaFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
    }


    private fun initView() {

        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val gestureOverlayView = findViewById<RelativeLayout>(R.id.drawing_pad)
        val buttonConfirm: ImageButton
        buttonConfirm = findViewById(R.id.btnConfirm)
        mediaFile = outputMediaFileUri
        drawingView = SignatureView(this@EmployeeAuthorizationActivity, null, gestureOverlayView, mainLayout)
        gestureOverlayView.addView(drawingView)
        val display = this@EmployeeAuthorizationActivity.resources.displayMetrics
        val params = gestureOverlayView.layoutParams
        params.height = (display.heightPixels - UIUtils.convertDpToPixel(196f, this@EmployeeAuthorizationActivity)).toInt()
        params.width = (display.widthPixels - UIUtils.convertDpToPixel(100f, this@EmployeeAuthorizationActivity)).toInt()
        gestureOverlayView.layoutParams = params
        buttonConfirm.setOnClickListener { v: View? ->
            if (drawingView!!.isDraw) {
                saveDrawing()
            }
        }
        findViewById<View>(R.id.btnRemove).setOnClickListener { v: View? ->
            onBackPressed()
        }
        findViewById<View>(R.id.btnClear).setOnClickListener { v: View? -> drawingView!!.onClickClear() }

    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_authorization)
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
            "1234", Constants.MEDIA_TYPE_EMPLOYEE_SIGNATURE
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

            DialogUtils.showAlertDialog(this,getString(R.string.sign_successfully),object :DialogButtonClickListener{
                override fun onButtonClick() {
                    goToHomeActivity(this@EmployeeAuthorizationActivity)
                }
            })
        } catch (e: Exception) {
            Log.v("Signature Gestures", e.toString())
            e.printStackTrace()
        }
    }
}