package com.sphe.mymusic.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import com.sphe.base.storge.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Provides
    fun providePreferenceStore(app: Application): PreferencesStore = PreferencesStore(app.applicationContext)

    @Provides
    fun contentResolver(app: Application): ContentResolver = app.contentResolver
}