package lnbti.charithgtp01.smartattendanceuserapp.interfaces

/**
 * Listener to get the entered value in Dialog
 */
interface ValueSubmitDialogListener {
    fun onPositiveButtonClicked(value: String)
    fun onNegativeButtonClicked()
}
