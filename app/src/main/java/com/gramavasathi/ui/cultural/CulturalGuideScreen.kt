package com.gramavasathi.ui.cultural

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gramavasathi.ui.theme.WarmBeige

@Composable
fun CulturalGuideScreen() {
    val sections = listOf(
        "How to Greet Locals 🙏" to listOf("Namaste vs handshake context", "Use host's regional language greeting (Kannada: Namaskara)", "Remove shoes at house entrance"),
        "What to Wear 👗" to listOf("Modest clothing in villages", "Avoid sleeveless / shorts in common areas", "Carry a light dupatta/scarf"),
        "Food Etiquette 🍛" to listOf("Eat with right hand (rural norm)", "Accept what's offered graciously", "Ask before taking second helping"),
        "Village Time vs City Time ⏰" to listOf("Days start at 5 AM", "Power cuts are normal — carry a torch", "No 24/7 internet — enjoy the disconnect"),
        "Do's and Don'ts ✅❌" to listOf("Do: Help with small chores if you wish", "Do: Ask before photographing people", "Don't: Complain about simple amenities", "Don't: Waste water or food")
    )
    val expanded = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(Modifier.background(WarmBeige).padding(16.dp)) {
        items(sections) { (title, content) ->
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { expanded[title] = !(expanded[title] ?: false) }) {
                Column(Modifier.padding(12.dp)) {
                    Text("$title ${if (expanded[title] == true) "−" else "+"}", style = MaterialTheme.typography.titleLarge)
                    AnimatedVisibility(visible = expanded[title] == true) {
                        Column { content.forEach { Text("• $it", modifier = Modifier.padding(top = 6.dp)) } }
                    }
                }
            }
        }
    }
}
