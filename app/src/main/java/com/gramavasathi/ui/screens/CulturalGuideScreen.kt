package com.gramavasathi.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gramavasathi.ui.theme.*

@Composable
fun CulturalGuideScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBeige)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(text = "Village Etiquette", style = MaterialTheme.typography.displayLarge, fontSize = 28.sp)
        Text(text = "A guest is like God (Atithi Devo Bhava)", color = MutedBrown, modifier = Modifier.padding(top = 4.dp))
        
        Box(modifier = Modifier.padding(top = 16.dp).width(60.dp).height(4.dp).background(Terracotta))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        GuideSection(
            title = "How to Greet Locals 🙏",
            content = "• Use 'Namaskara' in Karnataka villages.\n• A simple nod and smile go a long way.\n• Always remove your shoes before entering a village home."
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GuideSection(
            title = "What to Wear 👗",
            content = "• Dress modestly. Avoid sleeveless tops or short shorts.\n• Cotton clothes are best for the climate.\n• Carry a light scarf or dupatta."
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GuideSection(
            title = "Food Etiquette 🍛",
            content = "• Eat with your right hand; it's the norm.\n• Accept what's offered graciously.\n• Ask before taking a second helping."
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GuideSection(
            title = "Village Time vs City Time ⏰",
            content = "• Days start early (5 AM).\n• Power cuts are normal—carry a torch.\n• No 24/7 internet—enjoy the disconnect."
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GuideSection(
            title = "Do's and Don'ts ✅❌",
            content = "• DO: Help with small chores if you wish.\n• DO: Ask before photographing people.\n• DON'T: Complain about simple amenities.\n• DON'T: Waste water or food."
        )
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun GuideSection(title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CreamWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 18.sp
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation),
                    tint = EarthBrown
                )
            }
            
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = content,
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MutedBrown,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
