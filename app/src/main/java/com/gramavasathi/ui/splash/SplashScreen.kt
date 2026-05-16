package com.gramavasathi.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.EarthBrown
import com.gramavasathi.ui.theme.MutedBrown

@Composable
fun SplashScreen(onDone: () -> Unit, vm: SplashViewModel = viewModel()) {
    var step by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        vm.start(onDone)
        kotlinx.coroutines.delay(800)
        step = 1
        kotlinx.coroutines.delay(600)
        step = 2
    }
    Column(
        modifier = Modifier.fillMaxSize().background(CreamWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🏡🌾")
        AnimatedVisibility(visible = step >= 1) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ग्राम वसति", color = EarthBrown, fontSize = 36.sp)
                Text("Grama-Vasathi", color = MutedBrown, fontSize = 18.sp)
            }
        }
        AnimatedVisibility(visible = step >= 2) {
            Text("Live the Matti-Vasane", color = MutedBrown)
        }
    }
}
