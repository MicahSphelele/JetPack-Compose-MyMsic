package com.sphe.mymusic.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import com.sphe.base.base.CoroutineDispatchers
import com.sphe.base.storge.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideAppContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideAppContentResolver(app: Application): ContentResolver =
        app.applicationContext.contentResolver

    @Provides
    fun providePreferenceStore(app: Application): PreferencesStore =
        PreferencesStore(app.applicationContext)

    @Provides
    fun contentResolver(app: Application): ContentResolver = app.contentResolver

    @Singleton
    @Provides
    fun coroutineDispatchers() = CoroutineDispatchers(
        network = Dispatchers.IO,
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )
}