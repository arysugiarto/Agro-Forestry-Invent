package com.agro.inventory.data.di

import com.agro.inventory.data.remote.api.ApiCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideApiCallback(retrofit: Retrofit): ApiCallback =
        retrofit.create(ApiCallback::class.java)

}