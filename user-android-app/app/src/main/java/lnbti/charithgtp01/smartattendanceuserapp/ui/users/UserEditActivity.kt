package lnbti.charithgtp01.smartattendanceuserapp.ui.users

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.ui.users.UserDetailsViewModel
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityUserDetailsBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.User

@AndroidEntryPoint
class UserEditActivity : AppCompatActivity() {
    private var binding: ActivityUserDetailsBinding? = null
    private lateinit var viewModel: UserDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        setData()
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