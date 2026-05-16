package com.gramavasathi.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gramavasathi.R
import com.gramavasathi.Screen
import com.gramavasathi.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3500)
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(CreamWhite, WarmBeige))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale).alpha(alpha)
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(EarthBrown),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_host),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "ಗ್ರಾಮ ವಸತಿ",
                style = Typography.displayLarge,
                fontSize = 40.sp,
                color = EarthBrown
            )
            
            Text(
                text = "GRAMA-VASATHI",
                style = Typography.labelMedium,
                letterSpacing = 4.sp,
                color = MutedBrown,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Box(modifier = Modifier.padding(top = 32.dp).width(40.dp).height(2.dp).background(Terracotta))
            
            Text(
                text = "Live the Matti-Vasane",
                style = Typography.bodyLarge,
                color = EarthBrown,
                modifier = Modifier.padding(top = 16.dp),
                fontWeight = FontWeight.Bold
            )
        }
        
        Text(
            text = "Pure. Rural. Authentic.",
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp).alpha(0.5f),
            style = Typography.labelMedium,
            color = MutedBrown
        )
    }
}
