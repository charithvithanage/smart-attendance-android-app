package lnbti.charithgtp01.smartattendanceadminapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceadminapp.databinding.ActivityMainBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.SuccessListener
import lnbti.charithgtp01.smartattendanceadminapp.ui.login.LoginActivity
import lnbti.charithgtp01.smartattendanceadminapp.utils.DialogUtils.Companion.showConfirmAlertDialog
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils
import lnbti.charithgtp01.smartattendanceadminapp.utils.Utils.Companion.navigateWithoutHistory
import okhttp3.internal.Util

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()

    }

    private fun initView() {
        setSupportActionBar(binding.appBarMain.toolbar)
        setupNavigationComponents()

        binding.btnSignOut.setOnClickListener {
            showConfirmAlertDialog(
                this@MainActivity,
                getString(R.string.confirm_logout_message),
                object : ConfirmDialogButtonClickListener {
                    override fun onPositiveButtonClick() {
                        Utils.clearAllPref(this@MainActivity, object : SuccessListener {
                            override fun onFinished() {
                                navigateWithoutHistory(this@MainActivity, LoginActivity::class.java)
                            }
                        })
                    }

                    override fun onNegativeButtonClick() {

                    }
                })
        }

    }

    private fun setupNavigationComponents() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_users, R.id.nav_pending_approvals, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}