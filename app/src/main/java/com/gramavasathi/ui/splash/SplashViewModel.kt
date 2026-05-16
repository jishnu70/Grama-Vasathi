package com.gramavasathi.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SplashViewModel : ViewModel() {
    fun start(onDone: () -> Unit) {
        viewModelScope.launch {
            if (FirebaseAuth.getInstance().currentUser == null) {
                runCatching { FirebaseAuth.getInstance().signInAnonymously().await() }
            }
            delay(2500)
            onDone()
        }
    }
}

