package com.gramavasathi.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gramavasathi.R
import com.gramavasathi.Screen
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.EarthBrown
import com.gramavasathi.ui.theme.Lora
import com.gramavasathi.ui.theme.MutedBrown
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamWhite),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_host),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .alpha(alphaAnim.value),
                colorFilter = ColorFilter.tint(EarthBrown)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "ग्राम वसति",
                style = MaterialTheme.typography.displayLarge,
                fontSize = 36.sp,
                modifier = Modifier.alpha(alphaAnim.value)
            )
            
            Text(
                text = "Grama-Vasathi",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                color = MutedBrown,
                modifier = Modifier.alpha(alphaAnim.value)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Live the Matti-Vasane",
                style = MaterialTheme.typography.bodyMedium,
                color = EarthBrown,
                modifier = Modifier.alpha(alphaAnim.value)
            )
        }
    }
}
