package com.sphe.base.base.imageloading

import android.app.Application
import android.content.Context
import com.sphe.base.inititializer.AppInitializer
import javax.inject.Inject

class CoilAppInitializer @Inject constructor(private val context: Context) : AppInitializer {

    override fun init(application: Application) {

    }
}