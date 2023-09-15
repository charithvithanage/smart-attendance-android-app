package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapporvaldetails

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityPendingApprovalDetailsBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils

@AndroidEntryPoint
class PendingApprovalDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPendingApprovalDetailsBinding
    private lateinit var viewModel: PendingApprovalDetailsViewModel
    private var dialog: DialogFragment? = null
    private var error: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        setData()
        viewModelObservers()

    }

    private fun viewModelObservers() {

        //Waiting for Api response
        viewModel.pendingApprovalResult.observe(this@PendingApprovalDetailsActivity) {
            val apiResult = it

            dialog?.dismiss()

            if (apiResult?.success == true) {
                showAlertDialog(
                    this, Constants.SUCCESS,
                    getString(R.string.approval_submitted_successfully),
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            onBackPressed()

                        }

                    })
            } else if (apiResult?.data != null) {
                DialogUtils.showAlertDialog(
                    this, Constants.FAIL,
                    apiResult.data!!,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            onBackPressed()
                        }
                    })
            }
        }
    }

    private fun initView() {
        UIUtils.initiateActionBarWithoutHomeButton(
            binding.actionBar.mainLayout,
            getString(R.string.pending_approval_details)
        ) { onBackPressed() }

        binding.btnApprove.setOnClickListener {
            dialog = showProgressDialog(this, getString(R.string.wait))
            error = "Request Approved Successfully"
            viewModel.submitApproval(true)
        }

        binding.btnReject.setOnClickListener {
            dialog = showProgressDialog(this, getString(R.string.wait))
            error = "Request Rejected Successfully"
            viewModel.submitApproval(false)
        }
    }


    private fun setData() {
        val gson = Gson()
        val objectString = intent.getStringExtra(OBJECT_STRING)
        val pendingApprovalUser = gson.fromJson(objectString, User::class.java)
        viewModel.setPendingApprovalUserData(pendingApprovalUser)

    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pending_approval_details)
        viewModel = ViewModelProvider(this)[PendingApprovalDetailsViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }
}