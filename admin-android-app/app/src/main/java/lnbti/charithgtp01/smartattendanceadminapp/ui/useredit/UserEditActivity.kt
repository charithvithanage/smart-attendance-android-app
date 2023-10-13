package lnbti.charithgtp01.smartattendanceadminapp.ui.useredit

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.RadioButton
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
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.TAG
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityUserDetailsBinding
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityUserEditBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.ui.userdetails.UserDetailsViewModel
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.initiateActionBarWithoutHomeButton
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.goToHomeActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class UserEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserEditBinding
    private lateinit var viewModel: UserEditViewModel
    lateinit var pendingApprovalUser:User
    private val gson = Gson()
    private val cal = Calendar.getInstance()
    private var dialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        setData()
        viewModelObservers()
    }

    private fun initView() {
        initiateActionBarWithoutHomeButton(
            binding.actionBar.mainLayout,
            getString(R.string.edit_user)
        ) { onBackPressed() }

        binding.etDOB.setOnClickListener {
            DatePickerDialog(
                this@UserEditActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Observe data from the inner ViewModel (RadioGroup) using the outer ViewModel
        binding.selectGenderLayout.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            run {
                val radioButton: RadioButton = findViewById(checkedId)
                val selectedValue = radioButton.text.toString()
                viewModel.setGenderValues(selectedValue)
            }
        }

        binding.selectUserStatusLayout.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            run {
                val radioButton: RadioButton = findViewById(checkedId)
                val selectedValue = radioButton.text.toString()
                viewModel.setStatusValues(selectedValue)
            }
        }


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = Utils.spinnerItems[position]
                viewModel.selectUserRole(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when nothing is selected (optional)
            }
        }


        binding.btnUpdate.setOnClickListener {
            dialog = DialogUtils.showProgressDialog(this, getString(R.string.wait))
            viewModel.updateUser()
        }
    }

    private fun setData() {
        val objectString = intent.getStringExtra(OBJECT_STRING)
        pendingApprovalUser = gson.fromJson(objectString, User::class.java)
        viewModel.setUserData(pendingApprovalUser)
    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_edit)
        viewModel = ViewModelProvider(this)[UserEditViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun viewModelObservers() {

        //Waiting for Api response
        viewModel.serverResult.observe(this@UserEditActivity) {
            val apiResult = it

            dialog?.dismiss()

            if (apiResult?.success == true) {
                DialogUtils.showAlertDialog(
                    this, Constants.SUCCESS,
                    apiResult.message,
                    object : CustomAlertDialogListener {
                        override fun onDialogButtonClicked() {
                            goToHomeActivity(this@UserEditActivity)
                        }
                    })
            } else {
                DialogUtils.showErrorDialog(this, apiResult?.message)
            }
        }
    }


    /**
     * Date Picker Dialog Listener
     */
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat(getString(R.string.date_format), Locale.ENGLISH)
            binding.etDOB.setText(sdf.format(cal.time))

        }
}