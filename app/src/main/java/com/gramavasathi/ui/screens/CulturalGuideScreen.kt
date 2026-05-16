package com.gramavasathi.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gramavasathi.R
import com.gramavasathi.ui.theme.*

@Composable
fun CulturalGuideScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    
    Box(modifier = Modifier.fillMaxSize().background(CreamWhite)) {
        // Background Decorative Elements
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .blur(80.dp)
                .background(GoldenWheat.copy(alpha = 0.15f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 40.dp)
        ) {
            // Hero Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                // Background Pattern/Image
                Image(
                    painter = painterResource(id = R.drawable.ic_host), // Placeholder for pattern
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().graphicsLayer { alpha = 0.05f },
                    contentScale = ContentScale.Crop
                )
                
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Surface(
                        color = Terracotta,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "ETHOS & ETIQUETTE",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = Typography.labelMedium,
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                    Text(
                        text = "Village Guide",
                        style = Typography.displayLarge,
                        fontSize = 32.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Atithi Devo Bhava — The guest is God.",
                        style = Typography.bodyLarge,
                        color = MutedBrown
                    )
                }
            }

            // Guide Sections
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GuidePremiumSection(
                    icon = "🙏",
                    title = "Greeting & Entrance",
                    content = "• Greet elders with 'Namaskara' or 'Namaste'.\n• Always remove footwear before entering a home.\n• It is polite to wait until you are invited to sit."
                )
                
                GuidePremiumSection(
                    icon = "👗",
                    title = "Modest Attire",
                    content = "• Village life is traditional; please dress modestly.\n• Avoid short clothes or sleeveless tops in community areas.\n• Carrying a light scarf is always helpful."
                )
                
                GuidePremiumSection(
                    icon = "🍛",
                    title = "Dining Etiquette",
                    content = "• Eat with your right hand; it's the cultural norm.\n• Accept food offerings with both hands.\n• Do not waste food; take only what you can finish."
                )
                
                GuidePremiumSection(
                    icon = "⏰",
                    title = "Living the Rhythm",
                    content = "• Villages wake up with the sun (around 5 AM).\n• Respect quiet hours after 9 PM.\n• Be patient with power outages—they are part of the charm."
                )
                
                GuidePremiumSection(
                    icon = "📸",
                    title = "Photography",
                    content = "• Always ask before taking photos of people or homes.\n• Some temple areas may strictly prohibit photography.\n• Avoid using drones without local permission."
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Final Note
            Card(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WarmBeige),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = EarthBrown)
                    Text(
                        text = "Your host is your local guardian. When in doubt, just ask!",
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp),
                        color = EarthBrown
                    )
                }
            }
        }
    }
}

@Composable
fun GuidePremiumSection(icon: String, title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, if (expanded) GoldenWheat else Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(WarmBeige, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 24.sp)
                }
                
                Text(
                    text = title,
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    style = Typography.titleLarge,
                    fontSize = 18.sp
                )
                
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation),
                        tint = MutedBrown
                    )
                }
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider(color = DividerWarm)
                    Text(
                        text = content,
                        modifier = Modifier.padding(top = 16.dp),
                        style = Typography.bodyLarge,
                        color = MutedBrown,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}
