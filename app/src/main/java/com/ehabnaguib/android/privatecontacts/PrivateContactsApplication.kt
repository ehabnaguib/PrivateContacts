package com.ehabnaguib.android.privatecontacts

import android.app.Application
import com.ehabnaguib.android.privatecontacts.database.ContactRepository

class PrivateContactsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ContactRepository.initialize(this)
    }
}