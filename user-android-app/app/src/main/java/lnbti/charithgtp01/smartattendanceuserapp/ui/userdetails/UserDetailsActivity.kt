package lnbti.charithgtp01.smartattendanceuserapp.ui.userdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.OBJECT_STRING
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityUserDetailsBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ActionBarListener
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.initiateActionBar
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils

/**
 * Selected User Details Page
 */
@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var viewModel: UserDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateDataBinding()
        setData()
    }
    /**
     * Set Selected User data to the view
     */
    private fun setData() {
        Gson().run {
            viewModel.setPendingApprovalUserData(fromJson(intent.getStringExtra(OBJECT_STRING), User::class.java))
        }
    }

    private fun initiateDataBinding() {
        binding = DataBindingUtil.setContentView<ActivityUserDetailsBinding?>(
            this,
            R.layout.activity_user_details
        ).apply {
            viewModel =
                ViewModelProvider(this@UserDetailsActivity)[UserDetailsViewModel::class.java]
            vm = viewModel
            lifecycleOwner = this@UserDetailsActivity

            initiateActionBar(
                actionBar.mainLayout,
                getString(R.string.user_detais),
                object : ActionBarListener {
                    override fun backPressed() {
                        onBackPressed()
                    }

                    override fun homePressed() {
                        Utils.goToHomeActivity(this@UserDetailsActivity)
                    }
                })
        }

    }
}