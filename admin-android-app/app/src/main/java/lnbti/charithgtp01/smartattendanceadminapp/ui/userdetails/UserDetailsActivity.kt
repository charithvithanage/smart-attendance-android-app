package lnbti.charithgtp01.smartattendanceadminapp.ui.userdetails

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
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceadminapp.model.User
import lnbti.charithgtp01.smartattendanceadminapp.ui.useredit.UserEditActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.initiateActionBarWithCustomButton
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.navigateToAnotherActivityWithExtras

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {
    private var binding: ActivityUserDetailsBinding? = null
    private lateinit var viewModel: UserDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        initView()
        setData()
    }

    private fun initView() {
        initiateActionBarWithCustomButton(
            binding?.actionBar?.mainLayout!!,
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
    }

    private fun setData() {
        val gson = Gson()
        val objectString = intent.getStringExtra(OBJECT_STRING)
        val pendingApprovalUser = gson.fromJson(objectString, User::class.java)
        viewModel.setPendingApprovalUserData(pendingApprovalUser)

    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details)
        viewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]
        binding?.vm = viewModel
        binding?.lifecycleOwner = this
    }
}