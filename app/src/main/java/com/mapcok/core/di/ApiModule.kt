package com.mapcok.core.di

import com.mapcok.data.api.UserService
import dagger.Binds
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
    retrofit: Retrofit
  ): UserService = retrofit.create(UserService::class.java)


}