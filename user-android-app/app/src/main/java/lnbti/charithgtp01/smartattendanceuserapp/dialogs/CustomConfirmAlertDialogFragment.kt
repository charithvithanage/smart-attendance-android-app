package lnbti.charithgtp01.smartattendanceuserapp.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentCustomConfirmAlertDialogBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.changeUiSize

/**
 * Custom Alert Dialog Fragment
 */
class CustomConfirmAlertDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomConfirmAlertDialogBinding

    companion object {
        private const val ARG_MESSAGE = "message"
        lateinit var dialogButtonClickListener: ConfirmDialogButtonClickListener
        fun newInstance(
            message: String?,
            dialogButtonClickListener: ConfirmDialogButtonClickListener
        ): CustomConfirmAlertDialogFragment {
            val fragment = CustomConfirmAlertDialogFragment()
            Companion.dialogButtonClickListener = dialogButtonClickListener
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
        binding = FragmentCustomConfirmAlertDialogBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getString(ARG_MESSAGE)
        //Dialog Width with horizontal margin
        changeUiSize(context, binding.dialogMainLayout, 1, 1, 30)
        //Icon width=(Device Width/5)
        changeUiSize(context, binding.icon, 1, 5)
        // Set data to the data binding variables
        binding.dialogMessage = message
        binding.buttonYes.setOnClickListener {
            dialogButtonClickListener.onPositiveButtonClick()
            dismiss()
        }


        binding.buttonNo.setOnClickListener {
            dialogButtonClickListener.onNegativeButtonClick()
            dismiss()
        }


    }

}