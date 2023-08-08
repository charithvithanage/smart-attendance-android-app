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
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.changeUiSize

/**
 * Custom Alert Dialog Fragment
 */
class CustomAlertDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomAlertDialogBinding

    companion object {
        private const val ARG_MESSAGE = "message"
        private const val ARG_TYPE = "type"
        lateinit var dialogButtonClickListener: CustomAlertDialogListener
        var isErrorDialog = false
        fun newInstance(
            message: String?,
            type: String,
            dialogButtonClickListener: CustomAlertDialogListener
        ): CustomAlertDialogFragment {
            val fragment = CustomAlertDialogFragment()
            this.dialogButtonClickListener = dialogButtonClickListener
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
                putString(ARG_TYPE, type)
            }
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            message: String?
        ): CustomAlertDialogFragment {
            isErrorDialog = true
            val fragment = CustomAlertDialogFragment()
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
        binding = FragmentCustomAlertDialogBinding.inflate(inflater, container, false)
//        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getString(ARG_MESSAGE)
        val type = arguments?.getString(ARG_TYPE)
        //Dialog Width with horizontal margin
        changeUiSize(context, binding.dialogMainLayout, 1, 1, 30)
        //Icon width=(Device Width/5)
        changeUiSize(context, binding.icon, 1, 5)
        // Set data to the data binding variables
        binding.dialogMessage = message
        if(isErrorDialog){
            binding.imageResId = R.mipmap.cancel
        }else{
            if (type == Constants.SUCCESS)
                binding.imageResId = R.mipmap.done
            else if (type == Constants.FAIL)
                binding.imageResId = R.mipmap.cancel
        }


        binding.button.setOnClickListener {
            //Error Dialog should not want to return button click listener
            if (!isErrorDialog)
                dialogButtonClickListener.onDialogButtonClicked()
            dismiss()
        }

    }

}