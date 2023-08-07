package lnbti.charithgtp01.smartattendanceadminapp.ui.useredit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityUserDetailsBinding
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityUserEditBinding
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.ui.userdetails.UserDetailsViewModel
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils

@AndroidEntryPoint
class UserEditActivity : AppCompatActivity() {
    private var binding: ActivityUserEditBinding? = null
    private lateinit var viewModel: UserDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        setData()
    }

    private fun initView() {
        UIUtils.initiateActionBarWithoutHomeButton(
            binding?.actionBar?.mainLayout!!,
            getString(R.string.edit_user)
        ) { onBackPressed() }

        binding?.btnUpdate?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setData() {
        val gson = Gson()
        val objectString = intent.getStringExtra(OBJECT_STRING)
        val pendingApprovalUser = gson.fromJson(objectString, User::class.java)
        viewModel.setPendingApprovalUserData(pendingApprovalUser)
        /* Show profile icon using Glide */
        binding?.ownerIconView?.let { Glide.with(this).load(pendingApprovalUser.avatar).into(it) }

    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_edit)
        viewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
    }
}