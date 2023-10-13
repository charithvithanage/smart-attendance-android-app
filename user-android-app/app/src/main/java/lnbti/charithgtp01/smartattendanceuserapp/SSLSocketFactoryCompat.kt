package lnbti.charithgtp01.smartattendanceuserapp

import android.os.Build
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SSLSocketFactoryCompat {
    companion object {
        fun createSSLSocketFactory(): SSLSocketFactory {
            val trustAllCertificates = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    }

                    override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            try {
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, trustAllCertificates, null)
                return sslContext.socketFactory
            } catch (e: Exception) {
                throw e
            }
        }

        val trustManager: X509TrustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        }
    }
}