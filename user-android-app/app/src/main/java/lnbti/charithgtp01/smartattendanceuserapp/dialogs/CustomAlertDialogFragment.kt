package lnbti.charithgtp01.smartattendanceuserapp.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.R
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentCustomAlertDialogBinding
import lnbti.charithgtp01.smartattendanceuserapp.interfaces.CustomAlertDialogListener
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.changeUiSize

/**
 * Custom Alert Dialog Fragment
 */
class CustomAlertDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomAlertDialogBinding

    companion object {
        private const val ARG_MESSAGE = "message"
        private const val ARG_TYPE = "type"
        lateinit var dialogButtonClickListener: CustomAlertDialogListener
        private var hasDialogClickListener = false
        fun newInstance(
            message: String?,
            type: String,
            listener: CustomAlertDialogListener
        ): CustomAlertDialogFragment {
            return CustomAlertDialogFragment().apply {
                dialogButtonClickListener = listener
                hasDialogClickListener = true
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                    putString(ARG_TYPE, type)
                }
            }
        }

        fun newInstance(
            message: String?
        ): CustomAlertDialogFragment = CustomAlertDialogFragment().apply {
            hasDialogClickListener = false
            arguments = Bundle().apply {
                putString(ARG_MESSAGE, message)
                putString(ARG_TYPE, Constants.FAIL)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            // Remove dialog unwanted bg color in the corners
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // Disable outside click dialog dismiss event
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Disable back button pressed dialog dismiss event
        isCancelable = false
        binding = FragmentCustomAlertDialogBinding.inflate(inflater, container, false).apply {
            // vm = viewModel
            lifecycleOwner = this@CustomAlertDialogFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //Dialog Width with horizontal margin
            changeUiSize(context, dialogMainLayout, 1, 1, 30)
            //Icon width=(Device Width/5)
            changeUiSize(context, icon, 1, 5)
            // Set data to the data binding variables
            dialogMessage = arguments?.getString(ARG_MESSAGE)
            arguments?.getString(ARG_TYPE).apply {

                when {
                    this == Constants.SUCCESS -> imageResId = R.mipmap.done
                    this == Constants.FAIL -> imageResId = R.mipmap.cancel
                }
            }

            button.setOnClickListener {
                //Error Dialog should not want to return button click listener
                when {
                    hasDialogClickListener -> dialogButtonClickListener.onDialogButtonClicked()
                }
                dismiss()
            }

        }

    }

}