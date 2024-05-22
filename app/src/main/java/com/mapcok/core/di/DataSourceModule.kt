package com.mapcok.core.di

import com.mapcok.data.datasource.remote.GptDataSource
import com.mapcok.data.datasource.remote.GptDataSourceImpl
import com.mapcok.data.datasource.remote.PostDataSource
import com.mapcok.data.datasource.remote.PostDataSourceImpl
import com.mapcok.data.datasource.remote.UserDataSource
import com.mapcok.data.datasource.remote.UserDataSourceImpl
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
  fun provideUserDataSource(
    userDataSourceImpl: UserDataSourceImpl
  ): UserDataSource

  @Singleton
  @Binds
  fun provideUserPhotoDataSource(
    postDataSourceImpl: PostDataSourceImpl
  ): PostDataSource

  @Singleton
  @Binds
  fun provideGptDataSource(
    gptDataSourceImpl: GptDataSourceImpl
  ): GptDataSource
}

