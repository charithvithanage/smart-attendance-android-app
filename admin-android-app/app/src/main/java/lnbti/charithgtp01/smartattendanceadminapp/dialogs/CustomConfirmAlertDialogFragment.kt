package lnbti.charithgtp01.smartattendanceadminapp.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import lnbti.charithgtp01.smartattendanceadminapp.R
import lnbti.charithgtp01.smartattendanceadminapp.databinding.FragmentCustomConfirmAlertDialogBinding
import lnbti.charithgtp01.smartattendanceadminapp.interfaces.ConfirmDialogButtonClickListener
import lnbti.charithgtp01.smartattendanceadminapp.utils.UIUtils.Companion.changeUiSize

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
            fragment.isCancelable = false
            this.dialogButtonClickListener = dialogButtonClickListener
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
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