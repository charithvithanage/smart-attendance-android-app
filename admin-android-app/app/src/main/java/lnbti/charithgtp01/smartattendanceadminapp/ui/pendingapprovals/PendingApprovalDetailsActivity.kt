package lnbti.charithgtp01.smartattendanceadminapp.ui.pendingapprovals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityPendingApprovalDetailsBinding
import lnbti.charithgtp01.smartattendanceadminapp.model.User

@AndroidEntryPoint
class PendingApprovalDetailsActivity : AppCompatActivity() {
    private var binding: ActivityPendingApprovalDetailsBinding? = null
    private lateinit var viewModel: PendingApprovalDetailsViewModel
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pending_approval_details)
        viewModel = ViewModelProvider(this)[PendingApprovalDetailsViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
    }
}