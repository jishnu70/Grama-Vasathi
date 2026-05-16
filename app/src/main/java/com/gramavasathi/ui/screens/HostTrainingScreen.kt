package com.gramavasathi.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gramavasathi.R
import com.gramavasathi.ui.viewmodel.HostViewModel
import com.gramavasathi.ui.theme.*

@Composable
fun HostTrainingScreen(navController: NavController, viewModel: HostViewModel = viewModel()) {
    val currentStep by viewModel.currentStep.observeAsState(0)
    val score by viewModel.score.observeAsState(0)
    val checkedItems by viewModel.checkedItems.observeAsState(emptySet())
    
    val showFinalScore = currentStep >= viewModel.categories.size
    
    Box(modifier = Modifier.fillMaxSize().background(CreamWhite)) {
        if (!showFinalScore) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Step Indicator
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(viewModel.categories.size) { index ->
                        val color = if (index <= currentStep) GoldenWheat else DividerWarm
                        val size = if (index == currentStep) 14.dp else 10.dp
                        Box(modifier = Modifier.size(size).clip(CircleShape).background(color))
                        if (index < viewModel.categories.size - 1) {
                            Box(modifier = Modifier.width(20.dp).height(1.dp).background(DividerWarm))
                        }
                    }
                }

                val category = viewModel.categories[currentStep]
                val items = viewModel.getItemsForCategory(category)

                LazyColumn(
                    modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_host),
                                contentDescription = null,
                                tint = EarthBrown,
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = category,
                                style = MaterialTheme.typography.displayLarge,
                                fontSize = 22.sp,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }

                    items(items) { item ->
                        ChecklistItemView(
                            item = item,
                            isChecked = checkedItems.contains(item.id),
                            onToggle = { viewModel.toggleItem(item.id) }
                        )
                    }
                }

                // Sticky Score Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 16.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Your Score So Far:", color = MutedBrown, fontSize = 12.sp)
                                Text(text = "$score / 100", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp)
                            }
                            Surface(color = getScoreColor(score), shape = RoundedCornerShape(50.dp)) {
                                Text(
                                    text = getScoreTier(score),
                                    color = EarthBrown,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        LinearProgressIndicator(
                            progress = score / 100f,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = getScoreColor(score),
                            trackColor = DividerWarm
                        )
                        
                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (currentStep > 0) {
                                TextButton(
                                    onClick = { viewModel.prevStep() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("← Back", color = MutedBrown)
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            Button(
                                onClick = { viewModel.nextStep() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = EarthBrown),
                                shape = RoundedCornerShape(50.dp)
                            ) {
                                Text(if (currentStep == viewModel.categories.size - 1) "See Final Score →" else "Next Step →")
                            }
                        }
                    }
                }
            }
        } else {
            FinalScoreOverlay(score, viewModel) {
                // navController.navigateUp()
            }
        }
    }
}

@Composable
fun ChecklistItemView(item: com.gramavasathi.data.model.ChecklistItem, isChecked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onToggle() }.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = GoldenWheat)
        )
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(text = item.title, fontWeight = FontWeight.Bold, color = EarthBrown)
            Text(text = item.description, color = MutedBrown, fontSize = 13.sp)
        }
        Surface(color = WarmBeige, shape = RoundedCornerShape(50.dp)) {
            Text(
                text = "+${item.points} pts",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FinalScoreOverlay(score: Int, viewModel: HostViewModel, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = score / 100f,
                modifier = Modifier.size(200.dp),
                color = getScoreColor(score),
                strokeWidth = 12.dp,
                trackColor = DividerWarm
            )
            Text(text = score.toString(), style = MaterialTheme.typography.displayLarge, fontSize = 48.sp)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = getScoreTier(score), style = MaterialTheme.typography.displayLarge, fontSize = 24.sp)
        
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = WarmBeige),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                viewModel.categories.forEach { category ->
                    val (earned, max) = viewModel.getCategoryScore(category)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = category, fontWeight = FontWeight.Bold)
                        Text(text = "$earned/$max ${if (earned == max) "✓" else ""}")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            onClick = { /* Share */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Share My Score")
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { /* List Home-stay toast */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EarthBrown),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("List My Home-stay")
        }
    }
}

fun getScoreTier(score: Int): String {
    return when (score) {
        in 0..39 -> "🌱 Getting Started"
        in 40..69 -> "🌿 Almost Ready"
        in 70..89 -> "🌾 Guest Ready"
        else -> "⭐ Certified Host"
    }
}
