package lnbti.charithgtp01.smartattendanceuserapp.other

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticationHelper(private val context: Context) {
    fun authenticateBiometric(
        title: String,
        subtitle: String,
        description: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (isBiometricPromptAvailable()) {
            val executor = ContextCompat.getMainExecutor(context)
            val biometricPrompt = BiometricPrompt(
                context as FragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        onError(errString.toString())
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setDeviceCredentialAllowed(true) // Allow password as fallback
                .build()

            biometricPrompt.authenticate(promptInfo)
        } else {
            // Biometric authentication is not available on this device
            onError("Biometric authentication is not available on this device.")
        }
    }

    private fun isBiometricPromptAvailable(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }
}