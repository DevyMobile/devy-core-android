package com.devy.network.retrofit2

import com.devy.network.BuildConfig
import com.devy.network.retrofit2.adapter.Iso8601ToLocalDateTimeAdapter
import com.devy.network.retrofit2.adapter.ResponseCallAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

@Module
@InstallIn(SingletonComponent::class)
object Retrofit2Module {
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BasicRetrofitType

    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LongRetrofitType

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FoundationMoshi

    @Provides
    @Singleton
    @FoundationMoshi
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .add(Iso8601ToLocalDateTimeAdapter())
        .build()


    @Provides
    @Singleton
    @BasicRetrofitType
    fun provideBasicOkhttpClientBuilder(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(Constants.TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(Constants.TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(Constants.TIMEOUT, TimeUnit.MILLISECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }

    @Provides
    @Singleton
    @BasicRetrofitType
    fun provideBasicRetrofitBuilder(
        @BasicRetrofitType builder: OkHttpClient.Builder,
        @FoundationMoshi moshi: Moshi,
    ): @BasicRetrofitType Retrofit.Builder {
        val okHttpClient = builder.build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addCallAdapterFactory(ResponseCallAdapter.Factory())
    }

    @Provides
    @Singleton
    @LongRetrofitType
    fun provideLongOkhttpClientBuilder(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient.Builder = OkHttpClient.Builder()
        .connectTimeout(Constants.TIMEOUT_LONG, TimeUnit.MILLISECONDS)
        .readTimeout(Constants.TIMEOUT_LONG, TimeUnit.MILLISECONDS)
        .writeTimeout(Constants.TIMEOUT_LONG, TimeUnit.MILLISECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }

    @Provides
    @Singleton
    @LongRetrofitType
    fun provideLongRetrofitBuilder(
        @LongRetrofitType builder: OkHttpClient.Builder,
        @FoundationMoshi moshi: Moshi,
    ): @LongRetrofitType Retrofit.Builder {
        val okHttpClient = builder.build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addCallAdapterFactory(ResponseCallAdapter.Factory())
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val jsonAdapter = Moshi.Builder().build().adapter(Any::class.java)
        return HttpLoggingInterceptor { message -> dLog(jsonAdapter, message) }.apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }
}

class Constants {
    companion object {
        const val TIMEOUT = 60_000L
        const val TIMEOUT_LONG = 60_000L * 5
    }
}

private fun dLog(jsonAdapter: JsonAdapter<Any>, message: String) {
    val tag = "OkHttp"
    val maxLength = 3800
    if (message.length > maxLength) {
        message.chunked(maxLength).forEach { Timber.tag(tag).d(it) }
    } else {
        var logMessage = message
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                logMessage = jsonAdapter.toJsonValue(message).toString()
            } catch (e: AssertionError) {
                Timber.tag(tag).d(e)
            }
        }
        Timber.tag(tag).d(logMessage)
    }
}