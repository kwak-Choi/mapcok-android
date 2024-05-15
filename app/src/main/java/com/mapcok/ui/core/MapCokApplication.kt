package com.mapcok.ui.core

import android.app.Application
import timber.log.Timber

class MapCokApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
  }
}