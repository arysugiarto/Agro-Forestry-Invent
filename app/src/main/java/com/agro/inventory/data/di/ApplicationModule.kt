package com.agro.inventory.data.di

import android.app.Application
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import com.agro.inventory.data.local.dao.InvenPlotDao
import com.agro.inventory.data.local.dao.InventDao
import com.agro.inventory.data.local.dao.ReInvenPlotDao
import com.agro.inventory.data.local.dao.ReInventDao
import com.agro.inventory.data.preferences.AccessManager
import com.agro.inventory.data.remote.api.ApiCallback
import com.agro.inventory.data.repositories.AuthRepository
import com.agro.inventory.data.repositories.HomeRepository
import com.agro.inventory.data.repositories.LocalRepository
import com.agro.inventory.data.source.data.AuthRemoteDataSource
import com.agro.inventory.data.source.data.HomeRemoteDataSource
import com.agro.inventory.data.source.data.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideApplication(application: Application): Context = application

    @Singleton
    @Provides
    fun provideAccessManager(context: Context) = AccessManager(context)

    @Provides
    @Singleton
    fun provideReviewManager(@ApplicationContext context: Context) =
        ReviewManagerFactory.create(context)

    @Provides
    fun provideHomeRepository(
        apiCallback: ApiCallback
    ) = HomeRepository(
        HomeRemoteDataSource(apiCallback)
    )

    @Provides
    fun provideAuthRepository(
        apiCallback: ApiCallback
    ) = AuthRepository(
        AuthRemoteDataSource(apiCallback)
    )

    @Provides
    fun provideLocalRepository(
        inventPlotDao: InvenPlotDao,
        reInventPlotDao: ReInvenPlotDao,
        inventDao: InventDao,
        reInventDao: ReInventDao,
        ) = LocalRepository(
        LocalDataSource(inventPlotDao, reInventPlotDao, inventDao, reInventDao)
    )

}