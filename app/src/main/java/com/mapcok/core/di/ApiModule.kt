package com.mapcok.core.di

import com.mapcok.data.api.GptService
import com.mapcok.data.api.PostService
import com.mapcok.data.api.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

  @Singleton
  @Provides
  fun provideUserService(
    @NetworkModule.BaseRetrofit retrofit: Retrofit
  ): UserService = retrofit.create(UserService::class.java)

  @Singleton
  @Provides
  fun providePostService(
    @NetworkModule.BaseRetrofit retrofit: Retrofit
  ): PostService = retrofit.create(PostService::class.java)

  @Singleton
  @Provides
  fun provideGptService(
    @NetworkModule.GptRetrofit retrofit : Retrofit
  ) : GptService = retrofit.create(GptService::class.java)

}
