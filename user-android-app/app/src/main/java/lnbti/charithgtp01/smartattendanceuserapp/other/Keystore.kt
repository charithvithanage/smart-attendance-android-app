package lnbti.charithgtp01.smartattendanceuserapp.other

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.MessageDigest
import java.security.Provider
import java.security.Security
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * Encryption Key store file
 */
class Keystore {

    companion object {

        const val ENCRYPTION_ALGORITHM = "AES/CBC/PKCS7Padding"
        const val PROVIDER = "BC"

        @Throws(Exception::class)
        fun encrypt(key: String, value: String): String? {
            val provider: Provider = BouncyCastleProvider()
            Security.addProvider(provider)
//            val secretKeySpec = SecretKeySpec(key.toByteArray(), "AES")
            val secretKeySpec = generateKey(key)
            val cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM, PROVIDER)
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            val encrypted = cipher.doFinal(value.toByteArray())
            val iv = cipher.iv
            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder()
                .encodeToString(encrypted)
        }

        @Throws(java.lang.Exception::class)
        fun generateKey(pwd: String): SecretKeySpec? {
            val digest =
                MessageDigest.getInstance("SHA-256")
            val bytes = pwd.toByteArray(charset("UTF-8"))
            digest.update(bytes, 0, bytes.size)
            val key = digest.digest()
            return SecretKeySpec(key, "AES")
        }

        @Throws(java.lang.Exception::class)
        fun decrypt(key: String, data: String): String? {
            val provider: Provider = BouncyCastleProvider()
            Security.addProvider(provider)
            val parts = data.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val iv = Base64.getDecoder().decode(parts[0])
            val encryptedData = Base64.getDecoder().decode(parts[1])
            val secretKeySpec = generateKey(key)
            val cipher: Cipher =
                Cipher.getInstance(ENCRYPTION_ALGORITHM, PROVIDER)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
            val decryptedBytes = cipher.doFinal(encryptedData)
            return String(decryptedBytes)
        }
    }




}