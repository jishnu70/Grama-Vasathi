package com.gramavasathi

import android.app.Application
import androidx.preference.PreferenceManager
import com.google.firebase.FirebaseApp
import org.osmdroid.config.Configuration

class GramaVasathiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        Configuration.getInstance().userAgentValue = "GramaVasathi/1.0"
    }
}
