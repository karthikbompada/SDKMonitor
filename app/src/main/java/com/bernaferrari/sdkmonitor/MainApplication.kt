package com.bernaferrari.sdkmonitor

import androidx.multidex.MultiDexApplication
import com.bernaferrari.sdkmonitor.core.AppManager
import com.facebook.stetho.Stetho
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class MainApplication : MultiDexApplication() {

    lateinit var component: SingletonComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        component = DaggerSingletonComponent.builder()
            .contextModule(ContextModule(this))
            .appModule(AppModule(this))
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        AppManager.init(this)
    }

    companion object {
        private var INSTANCE: MainApplication? = null

        @JvmStatic
        fun get(): MainApplication =
            INSTANCE ?: throw NullPointerException("MainApplication INSTANCE must not be null")
    }
}