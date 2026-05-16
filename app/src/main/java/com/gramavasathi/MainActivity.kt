package com.gramavasathi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gramavasathi.ui.GramaVasathiRoot
import com.gramavasathi.ui.theme.GramaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GramaTheme {
                GramaVasathiRoot()
            }
        }
    }
}
