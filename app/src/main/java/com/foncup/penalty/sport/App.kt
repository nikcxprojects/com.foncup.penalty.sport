package com.foncup.penalty.sport

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

class App : Application()  {
    private val YANDEX_API_KEY = "c23a24f8-e727-4bee-9756-585c3f905ad7"

    companion object{
        private lateinit var myApp: App
    }

    override fun onCreate() {
        super.onCreate()
        myApp = this
        val config = YandexMetricaConfig.newConfigBuilder(YANDEX_API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }

}