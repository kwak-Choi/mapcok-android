package com.mapcok.core.di

import com.google.gson.GsonBuilder
import com.mapcok.ui.util.SingletonUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Qualifier
  @Retention(AnnotationRetention.BINARY)
  annotation class BaseRetrofit

  @Qualifier
  @Retention(AnnotationRetention.BINARY)
  annotation class GptRetrofit

  @Singleton
  @Provides
  @BaseRetrofit
  fun provideBaseRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
      .baseUrl(SingletonUtil.baseUrl)
      .client(okHttpClient)
      .build()
  }

  @Singleton
  @Provides
  @GptRetrofit
  fun provideGptRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
      .baseUrl(SingletonUtil.gptUrl)
      .client(okHttpClient)
      .build()
  }




  @Singleton
  @Provides
  fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
    OkHttpClient.Builder()
      .run {
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(120, TimeUnit.SECONDS)
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        addInterceptor(interceptor)
        build()
      }

  @Singleton
  @Provides
  fun provideInterceptor(): Interceptor =
    Interceptor { chain ->
      with(chain) {
        val newRequest = request().newBuilder()
          .addHeader("Authorization", "Bearer ${SingletonUtil.chatGptApi}")
          .addHeader("Content-Type", "application/json")
          .build()

        proceed(newRequest)
      }
    }


}

