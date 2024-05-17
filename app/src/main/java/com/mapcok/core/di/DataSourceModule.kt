package com.mapcok.core.di

import com.mapcok.data.datasource.remote.UserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {


  @Singleton
  @Binds
  fun provideLoginDataSource(

  ) : UserDataSource
}