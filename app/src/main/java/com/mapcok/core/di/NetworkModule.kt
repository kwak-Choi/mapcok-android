package com.mapcok.core.di

import com.google.gson.GsonBuilder
import com.mapcok.ui.util.SingletonUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Singleton
  @Provides
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    //if(BuildConfig.DEBUG){BuildConfig.DEBUG_API_KEY} else{BuildConfig.API_KEY}
    val apiKey = SingletonUtil.baseUrl
    return Retrofit.Builder()
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
      .baseUrl(apiKey)
      .client(okHttpClient)
      .build()
  }


  @Singleton
  @Provides
  fun provideOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
      .run {
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(120, TimeUnit.SECONDS)
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        build()
      }

}