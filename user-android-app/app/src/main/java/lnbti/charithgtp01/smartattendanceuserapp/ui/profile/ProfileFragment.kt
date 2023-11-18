package lnbti.charithgtp01.smartattendanceuserapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.LOGGED_IN_USER
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentProfileBinding
import lnbti.charithgtp01.smartattendanceuserapp.model.User
import lnbti.charithgtp01.smartattendanceuserapp.utils.Utils

/**
 * A [Fragment] that displays the user profile information.
 *
 * This fragment uses data binding to bind UI elements and a [ProfileViewModel] to manage
 * the underlying data and UI logic.
 *
 * @constructor Creates a new instance of [ProfileFragment].
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*
         * Initiate Data Binding and View Model
        */
        ViewModelProvider(requireActivity())[ProfileViewModel::class.java].apply {
            viewModel = this
            FragmentProfileBinding.inflate(inflater, container, false).apply {
                binding = this
                vm = viewModel
                lifecycleOwner = this@ProfileFragment
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    /**
     * Sets the data for the user profile.
     */
    private fun setData() {
        Gson().apply {
            fromJson(
                Utils.getObjectFromSharedPref(requireContext(), LOGGED_IN_USER),
                User::class.java
            ).apply {
                viewModel.setUser(this)
            }
        }
    }
}