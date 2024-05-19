package com.mapcok.core.di

import com.mapcok.data.datasource.remote.UserDataSource
import com.mapcok.data.datasource.remote.UserDataSourceImpl
import com.mapcok.data.datasource.remote.UserPhotoDataSource
import com.mapcok.data.datasource.remote.UserPhotoDataSourceImpl
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
        userPhotoDataSourceImpl: UserPhotoDataSourceImpl
    ): UserPhotoDataSource
}
