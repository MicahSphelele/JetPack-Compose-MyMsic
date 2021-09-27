package com.sphe.models.notifications

import android.app.NotificationManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NotificationsModule {

    @Provides
    @Singleton
    fun providesNotifications(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): Notifications =
        RealNotifications(context, notificationManager)
}