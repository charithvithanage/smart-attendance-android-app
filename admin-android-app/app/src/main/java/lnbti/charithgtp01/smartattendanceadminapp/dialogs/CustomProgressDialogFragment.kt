package lnbti.charithgtp01.smartattendanceadminapp.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.constants.Constants
import lnbti.charithgtp01.smartattendanceadminapp.databinding.FragmentCustomAlertDialogBinding
import lnbti.charithgtp01.smartattendanceadminapp.databinding.FragmentCustomProgressDialogBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.changeUiSize

/**
 * Custom Alert Dialog Fragment
 */
class CustomProgressDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomProgressDialogBinding

    companion object {
        private const val ARG_MESSAGE = "message"

        fun newInstance(
            message: String?
        ): CustomProgressDialogFragment {
            val fragment = CustomProgressDialogFragment()
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), theme)
        //Remove dialog unwanted bg color in the corners
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        //Disable outside click dialog dismiss event
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //Disable back button pressed dialog dismiss event
        isCancelable = false;
        binding = FragmentCustomProgressDialogBinding.inflate(inflater, container, false)
//        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getString(ARG_MESSAGE)
        //Dialog Width with horizontal margin
        changeUiSize(context, binding.dialogMainLayout, 1, 1, 30)
        //Icon width=(Device Width/3)
        changeUiSize(context, binding.icon, 1, 5)
        // Set data to the data binding variables
        binding.dialogMessage = message

    }

}