package com.gramavasathi

import android.app.Application
import com.google.firebase.FirebaseApp
import org.osmdroid.config.Configuration

class GramaVasathiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val prefs = getSharedPreferences("osmdroid", MODE_PRIVATE)
        Configuration.getInstance().load(this, prefs)
        Configuration.getInstance().userAgentValue = "GramaVasathi/1.0"
    }
}
