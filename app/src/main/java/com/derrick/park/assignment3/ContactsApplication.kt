package com.derrick.park.assignment3

import android.app.Application
import timber.log.Timber

class ContactsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}