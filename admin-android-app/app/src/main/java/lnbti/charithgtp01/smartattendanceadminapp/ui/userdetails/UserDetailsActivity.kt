package lnbti.charithgtp01.smartattendanceadminapp.ui.userdetails

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceadminapp.constants.ResourceConstants
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityUserDetailsBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.ui.qr.DeviceIDQRActivity
import lnbti.charithgtp01.smartattendanceadminapp.ui.useredit.UserEditActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.initiateActionBarWithCustomButton
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var viewModel: UserDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        setData()
    }

    private fun initView() {
        initiateActionBarWithCustomButton(
            binding.actionBar.mainLayout,
            getString(R.string.user_details), R.mipmap.edit_white,
            object : ActionBarListener {
                override fun backPressed() {
                    onBackPressed()
                }

                override fun homePressed() {
                    val objectString = intent.getStringExtra(OBJECT_STRING)
                    val prefMap = HashMap<String, String>()
                    if (objectString != null) {
                        prefMap[OBJECT_STRING] = objectString
                        navigateToAnotherActivityWithExtras(
                            this@UserDetailsActivity,
                            UserEditActivity::class.java, prefMap
                        )
                    }

                }
            })

        binding.qrBtn.setOnClickListener {
            val navigationPathMap = HashMap<String, String>()
            navigationPathMap[OBJECT_STRING] = gson.toJson(pendingApprovalUser)
            navigateToAnotherActivityWithExtras(
                this@UserDetailsActivity,
                DeviceIDQRActivity::class.java, navigationPathMap
            )
        }

    }

    lateinit var pendingApprovalUser: User
    private var objectString: String? = null
    val gson = Gson()

    private fun setData() {
        objectString = intent.getStringExtra(OBJECT_STRING)
        pendingApprovalUser = gson.fromJson(objectString, User::class.java)
        viewModel.setPendingApprovalUserData(pendingApprovalUser)

        /**
         * This button is for generate selected user's nic QR
         * Then get a screenshot and print the QR
         */
        if (pendingApprovalUser.userType == "Other")
            binding.qrBtn.visibility = View.VISIBLE

    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details)
        viewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }
}