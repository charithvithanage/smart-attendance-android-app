package lnbti.charithgtp01.smartattendanceuserapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityMainBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.ui.login.LoginActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.settings.SettingsActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showConfirmAlertDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showErrorDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.DialogUtils.Companion.showProgressDialog
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.LOCATION_PERMISSION_REQUEST_CODE
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.checkPermissions
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.isLocationEnabled
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.logout
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.navigateWithoutHistory

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var locationPermissionGranted = false
    private lateinit var sharedViewModel: MainActivityViewModel
    private var dialog: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply {
                lifecycleOwner = this@MainActivity

                setSupportActionBar(toolbar)
                //If the logged in user's user role is Employee
                val navController = findNavController(R.id.navHostFragmentUser)
                bottomNavigationUser.setupWithNavController(navController)
                val userRole = getObjectFromSharedPref(this@MainActivity, Constants.USER_ROLE)
                if (userRole == getString(R.string.employee)) {
                    //Bottom menu without users menu
                    // Hide a menu item by ID
                    bottomNavigationUser.menu.findItem(R.id.nav_users).isVisible = false
                }

            }

        sharedViewModel =
            ViewModelProvider(this@MainActivity)[MainActivityViewModel::class.java].apply {
                checkPermission()

                // Observe loading state and show/hide a progress dialog
                isDialogVisible.observe(this@MainActivity) {
                    if (it) {
                        Log.d("DIALOG TEST", "Show Dialog")
                        dialog = showProgressDialog(this@MainActivity, getString(R.string.wait))
                    } else {
                        /* Dismiss dialog after updating the data list to recycle view */
                        Log.d("DIALOG TEST", "Dismiss Dialog")
                        dialog?.dismiss()
                    }
                }

                shouldOpenSettings.observe(this@MainActivity) {
                    when {
                        it -> {
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).run {
                                startActivity(this)
                            }
                        }
                    }
                }

                checkPermission.observe(this@MainActivity) {
                    when {
                        it -> {
                            getLocationPermissionAvailability()
                        }
                    }
                }

                errorMessage.observe(this@MainActivity) {
                    it?.let {
                        showErrorDialog(this@MainActivity, it)
                    }
                }
            }
    }

    fun getLocationPermissionAvailability() {
        /**
         * User Must enable location access to continue the attendance flow
         */
        if (checkPermissions(this)) {
            if (isLocationEnabled(this)) {
                locationPermissionGranted = true
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            Utils.requestPermissions(this@MainActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                Utils.navigateToAnotherActivity(this, SettingsActivity::class.java)
            }

            R.id.action_logout -> {
                showConfirmAlertDialog(
                    this@MainActivity,
                    getString(R.string.confirm_logout_message),
                    object : ConfirmDialogButtonClickListener {
                        override fun onPositiveButtonClick() {
                            logout(this@MainActivity) {
                                navigateWithoutHistory(
                                    this@MainActivity,
                                    LoginActivity::class.java
                                )
                            }

                        }

                        override fun onNegativeButtonClick() {

                        }
                    })

            }
        }
        return false
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(
                    applicationContext,
                    "GPS location permission granted", Toast.LENGTH_SHORT
                )
            }
        }
    }
}