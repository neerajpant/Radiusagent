package com.example.radiusagent.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.greedygame.api.NetworkApi
import com.example.radiusagent.database.PropertyDataBase


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(NetworkApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRestaurantApi(retrofit: Retrofit): NetworkApi =
        retrofit.create(NetworkApi::class.java)


    @Provides
    @Singleton
    fun provideDatabase(app: Application) : PropertyDataBase =
        Room.databaseBuilder(app, PropertyDataBase::class.java, "product_db")
            .fallbackToDestructiveMigration()
            .build()
}