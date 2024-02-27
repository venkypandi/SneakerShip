package com.venkatesh.sneakership.di

import com.venkatesh.sneakership.data.remote.SneakerDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiDataSource():SneakerDataSource{
        return SneakerDataSource()
    }
}