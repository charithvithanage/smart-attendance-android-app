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
        return  CustomConfirmAlertDialogFragment().apply {
            Companion.dialogButtonClickListener = dialogButtonClickListener
            arguments = Bundle().apply {
                putString(ARG_MESSAGE, message)
            }
          }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      return Dialog(requireContext(), theme).apply {
          //Remove dialog unwanted bg color in the corners
          window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
          //Disable outside click dialog dismiss event
          setCanceledOnTouchOutside(false)
      }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //Disable back button pressed dialog dismiss event
        isCancelable = false;
        binding = FragmentCustomConfirmAlertDialogBinding.inflate(inflater, container, false).apply {
            binding.lifecycleOwner = this@CustomConfirmAlertDialogFragment
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
            dialogMessage =   arguments?.getString(ARG_MESSAGE)

            buttonYes.setOnClickListener {
                dialogButtonClickListener.onPositiveButtonClick()
                dismiss()
            }

            buttonNo.setOnClickListener {
                dialogButtonClickListener.onNegativeButtonClick()
                dismiss()
            }
        }
    }
}