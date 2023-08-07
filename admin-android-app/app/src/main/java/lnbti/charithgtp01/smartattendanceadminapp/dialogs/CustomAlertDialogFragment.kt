package lnbti.charithgtp01.smartattendanceadminapp.dialogs

import android.app.Dialog
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

class CustomAlertDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomAlertDialogBinding

    companion object {
        private const val ARG_MESSAGE = "message"
        private const val ARG_TYPE = "type"
        lateinit var dialogButtonClickListener: CustomAlertDialogListener
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
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomAlertDialogBinding.inflate(inflater, container, false)
//        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getString(ARG_MESSAGE)
        val type = arguments?.getString(ARG_TYPE)
        changeUiSize(context, binding.dialogMainLayout, 1, 1, 30)
        changeUiSize(context, binding.icon, 1, 5)
        // Set data to the data binding variables
        binding.dialogMessage = message
        if (type == Constants.SUCCESS)
            binding.imageResId = R.mipmap.done
        else if (type == Constants.FAIL)
            binding.imageResId = R.mipmap.cancel



        binding.button.setOnClickListener {
            dialogButtonClickListener.onDialogButtonClicked()
            dismiss()
        }

    }

}