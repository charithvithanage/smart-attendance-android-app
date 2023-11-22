package lnbti.charithgtp01.smartattendanceuserapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnbti.charithgtp01.smartattendanceuserapp.other.SSLSocketFactoryCompat
import lnbti.charithgtp01.smartattendanceuserapp.apiservice.AttendanceService
import lnbti.charithgtp01.smartattendanceuserapp.apiservice.CompanyService
import lnbti.charithgtp01.smartattendanceuserapp.apiservice.UserService
import lnbti.charithgtp01.smartattendanceuserapp.constants.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Singleton Component Class for DI
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Application context
     */
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    /**
     * Get Base Url of the Rest API
     */
    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    /**
     * Create Gson Convertor Factory
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Create OkHttpClient
     * Add Interceptor with Headers
     */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val okHttpClient =
            OkHttpClient.Builder()
                .sslSocketFactory(
                    SSLSocketFactoryCompat.createSSLSocketFactory(),
                    SSLSocketFactoryCompat.trustManager
                ) // Use custom SSLSocketFactory and TrustManager

        return okHttpClient.build()
    }

    /**
     * Create Retrofit Instance
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    /**
     *  Abstract the communication with the remote API
     *  Create User Api Service Interface
     */
    @Singleton
    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideCompanyApiService(retrofit: Retrofit): CompanyService {
        return retrofit.create(CompanyService::class.java)
    }

    @Singleton
    @Provides
    fun provideAttendanceApiService(retrofit: Retrofit): AttendanceService {
        return retrofit.create(AttendanceService::class.java)
    }
}