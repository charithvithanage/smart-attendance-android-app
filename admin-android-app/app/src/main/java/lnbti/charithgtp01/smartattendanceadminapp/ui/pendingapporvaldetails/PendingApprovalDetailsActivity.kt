package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapporvaldetails

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityPendingApprovalDetailsBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showAlertDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showConfirmAlertDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.goToHomeActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.userRoles

/**
 * Pending Approval Details View/Approve/Reject page
 */
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

    private fun initView() {
        UIUtils.initiateActionBarWithoutHomeButton(
            binding.actionBar.mainLayout,
            getString(R.string.pending_approval_details)
        ) { onBackPressed() }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = viewModel.userRoleSpinnerItems[position]
                viewModel.selectUserRole(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected (optional)
            }
        }

        binding.spinnerUserType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = viewModel.userTypeSpinnerItems[position]
                    viewModel.selectUserType(selectedItem)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle nothing selected if needed
                }
            }


        binding.btnApprove.setOnClickListener {
            if (binding.deviceIDLayout.editText?.text.isNullOrBlank()) {
                showErrorDialog(
                    this@PendingApprovalDetailsActivity,
                    getString(R.string.device_id_mandatory)
                )
            } else {
                viewModel.submitApproval(
                    pendingApprovalUser.nic,
                    viewModel.deviceID,
                    pendingApprovalUser.userRole,
                    true
                )
            }
        }

        binding.btnReject.setOnClickListener {
            showConfirmAlertDialog(
                this@PendingApprovalDetailsActivity,
                getString(R.string.confirm_reject_message),
                object : ConfirmDialogButtonClickListener {
                    override fun onPositiveButtonClick() {
                        viewModel.rejectApproval(pendingApprovalUser.nic)
                    }

                    override fun onNegativeButtonClick() {

                    }
                })


        }
    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pending_approval_details)
        viewModel = ViewModelProvider(this)[PendingApprovalDetailsViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    lateinit var pendingApprovalUser: User
    private fun setData() {
        val gson = Gson()
        val objectString = intent.getStringExtra(OBJECT_STRING)
        pendingApprovalUser = gson.fromJson(objectString, User::class.java)
        viewModel.setPendingApprovalUserData(pendingApprovalUser)
    }

    private fun viewModelObservers() {
        /* Show error message in the custom error dialog */
        viewModel.errorMessage.observe(this@PendingApprovalDetailsActivity) {
            showErrorDialog(this@PendingApprovalDetailsActivity, it)
        }

        viewModel.isDialogVisible.observe(this@PendingApprovalDetailsActivity) {
            if (it) {
                /* Show dialog when calling the API */
                dialog = showProgressDialog(this@PendingApprovalDetailsActivity, getString(R.string.wait))
            } else {
                /* Dismiss dialog after updating the data list to recycle view */
                dialog?.dismiss()
            }
        }

        //Waiting for Api response
        viewModel.pendingApprovalResult.observe(this@PendingApprovalDetailsActivity) {
            val apiResult = it

            dialog?.dismiss()

            if (apiResult?.success == true) {
                showAlertDialog(
                    this, Constants.SUCCESS,
                    apiResult.message,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            goToHomeActivity(this@PendingApprovalDetailsActivity)
                        }
                    })
            } else {
                showErrorDialog(this, apiResult?.message)
            }
        }
    }

}