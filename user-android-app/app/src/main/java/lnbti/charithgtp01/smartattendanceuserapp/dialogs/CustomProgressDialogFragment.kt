package lnbti.charithgtp01.smartattendanceuserapp.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import lnbti.charithgtp01.smartattendanceuserapp.databinding.FragmentCustomProgressDialogBinding
import lnbti.charithgtp01.smartattendanceuserapp.utils.UIUtils.Companion.changeUiSize

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
            return CustomProgressDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), theme).apply {
            //Remove dialog unwanted bg color in the corners
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //Disable outside click dialog dismiss event
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //Disable back button pressed dialog dismiss event
        isCancelable = false
        binding = FragmentCustomProgressDialogBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@CustomProgressDialogFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //Dialog Width with horizontal margin
            changeUiSize(context, dialogMainLayout, 1, 1, 30)
            //Icon width=(Device Width/3)
            changeUiSize(context, icon, 1, 5)
            // Set data to the data binding variables
            dialogMessage = arguments?.getString(ARG_MESSAGE)
        }
    }
}