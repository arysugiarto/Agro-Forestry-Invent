package com.agro.inventory.data.di

import android.content.Context
import androidx.room.Room
import com.agro.inventory.data.local.AgroDatabase
import com.agro.inventory.util.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
            Room.databaseBuilder(
                context,
                AgroDatabase::class.java,
                Const.Database.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

    @Singleton
    @Provides
    fun provideActivitiesDao(database: AgroDatabase) = database.activitiesDao()

    @Singleton
    @Provides
    fun provideAreaDao(database: AgroDatabase) = database.areaDao()

    @Singleton
    @Provides
    fun provideInventDao(database: AgroDatabase) = database.inventDao()

    @Singleton
    @Provides
    fun provideReInventDao(database: AgroDatabase) = database.reInventDao()



}