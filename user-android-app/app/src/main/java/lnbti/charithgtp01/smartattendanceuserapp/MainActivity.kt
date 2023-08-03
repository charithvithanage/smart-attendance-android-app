package lnbti.charithgtp01.smartattendanceuserapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.ActivityMainBinding
import lnbti.charithgtp01.smartattendanceuserapp.ui.login.LoginActivity
import lnbti.charithgtp01.smartattendanceuserapp.ui.settings.SettingsActivity
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.LOCATION_PERMISSION_REQUEST_CODE
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.checkPermissions
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.getObjectFromSharedPref
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils.Companion.isLocationEnabled


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var locationPermissionGranted = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)

        //If the logged in user's user role is Employee
        val userRole = getObjectFromSharedPref(this@MainActivity, Constants.USER_ROLE)
        if (userRole == getString(R.string.employee)) {
            //Bottom menu without users menu
            binding.userMainLayout.visibility = View.VISIBLE
            binding.businessUserMainLayout.visibility = View.GONE
            val navController = findNavController(R.id.navHostFragmentUser)
            binding.bottomNavigationUser.setupWithNavController(navController)
        } else {
            binding.userMainLayout.visibility = View.GONE
            binding.businessUserMainLayout.visibility = View.VISIBLE
            val navController = findNavController(R.id.navHostFragmentBusinessUser)
            binding.bottomNavigationBusinessUser.setupWithNavController(navController)

        }

        /**
         * User Must enable location access to continue the attendance flow
         */
        if (checkPermissions(this)) {
            if (isLocationEnabled(this)) {
                locationPermissionGranted = true;
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
                Utils.navigateWithoutHistory(this, LoginActivity::class.java)
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