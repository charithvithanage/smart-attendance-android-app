package lnbti.charithgtp01.smartattendanceuserapp

import android.util.Base64
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.SECURE_KEY
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.MessageDigest
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Encryption Key store file
 */
class Keystore {

    companion object {
        private const val AES = "AES"

        @Throws(Exception::class)
        fun decrypt(outputString: String?): String? {
            val key = generateKey(SECURE_KEY)
            val c = Cipher.getInstance(AES)
            return try {
                c.init(Cipher.DECRYPT_MODE, key)
                val decodedValue =
                    Base64.decode(outputString, Base64.DEFAULT)
                val decValue = c.doFinal(decodedValue)
                val str = String(decValue)
                str
            } catch (e: java.lang.Exception) {
                "Incorrect Key"
            }
        }

        @Throws(Exception::class)
        fun encrypt(data: String, password: String): String? {
            val key = generateKey(password)
            val c = Cipher.getInstance(AES)
            c.init(Cipher.ENCRYPT_MODE, key)
            val encVal = c.doFinal(data.toByteArray())
            return Base64.encodeToString(encVal, Base64.DEFAULT)
        }


        @Throws(Exception::class)
        fun generateKey(pwd: String): SecretKeySpec {
            val digest = MessageDigest.getInstance("SHA-256")
            val bytes = pwd.toByteArray(charset("UTF-8"))
            digest.update(bytes, 0, bytes.size)
            val key = digest.digest()
            return SecretKeySpec(key, "AES")
        }

        @Throws(IOException::class)
        fun compress(string: String): ByteArray? {
            val os = ByteArrayOutputStream(string.length)
            val gos = GZIPOutputStream(os)
            gos.write(string.toByteArray(charset("ISO-8859-1")))
            gos.close()
            val compressed = os.toByteArray()
            os.close()
            return compressed
        }

        @Throws(IOException::class)
        fun decompress(compressed: ByteArray?): String? {
            val BUFFER_SIZE = 32
            val `is` = ByteArrayInputStream(compressed)
            val gis = GZIPInputStream(`is`, BUFFER_SIZE)
            val string = StringBuilder()
            val data = ByteArray(BUFFER_SIZE)
            var bytesRead: Int
            while (gis.read(data).also { bytesRead = it } != -1) {
                string.append(String(data, 0, bytesRead))
            }
            gis.close()
            `is`.close()
            return string.toString()
        }
    }

}