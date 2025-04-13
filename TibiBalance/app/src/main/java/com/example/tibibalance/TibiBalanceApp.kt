package com.example.tibibalance

import android.app.Application
import dagger.hilt.android.HiltAndroidApp // <-- Importación necesaria

@HiltAndroidApp
class TibiBalanceApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
