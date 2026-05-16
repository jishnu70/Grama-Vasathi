package com.gramavasathi.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gramavasathi.R
import com.gramavasathi.ui.theme.*
import com.gramavasathi.ui.viewmodel.HostViewModel
import com.gramavasathi.utils.*

@Composable
fun HostTrainingScreen(navController: NavController, viewModel: HostViewModel = viewModel()) {
    val currentStep by viewModel.currentStep.observeAsState(0)
    val score by viewModel.score.observeAsState(0)
    val checkedItems by viewModel.checkedItems.observeAsState(emptySet())
    
    val showFinalScore = currentStep >= viewModel.categories.size
    
    Box(modifier = Modifier.fillMaxSize().background(CreamWhite)) {
        if (!showFinalScore) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Premium Progress Header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Host Certification", style = Typography.labelMedium, color = MutedBrown)
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(viewModel.categories.size) { index ->
                                val isActive = index == currentStep
                                val isDone = index < currentStep
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(
                                            when {
                                                isDone -> LeafGreen
                                                isActive -> EarthBrown
                                                else -> DividerWarm
                                            }
                                        )
                                )
                            }
                        }
                    }
                }

                val category = viewModel.categories[currentStep]
                val items = viewModel.getItemsForCategory(category)

                LazyColumn(
                    modifier = Modifier.weight(1f).padding(horizontal = 20.dp),
                    contentPadding = PaddingValues(top = 24.dp, bottom = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = "Step ${currentStep + 1}: $category",
                                style = Typography.displayLarge,
                                fontSize = 24.sp
                            )
                            Text(
                                text = "Ensure your home meets these standards for city guests.",
                                style = Typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    items(items) { item ->
                        PremiumChecklistItem(
                            item = item,
                            isChecked = checkedItems.contains(item.id),
                            onToggle = { viewModel.toggleItem(item.id) }
                        )
                    }
                }
            }

            // Floating Score Card
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .fillMaxWidth(),
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 12.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Readiness Score", style = Typography.labelMedium, color = MutedBrown)
                            Text(text = "$score / 100", style = Typography.titleLarge, color = EarthBrown)
                        }
                        Surface(
                            color = getScoreColor(score).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = getScoreTier(score).split(" ").last(), // Icon only or short text
                                color = getScoreColor(score),
                                style = Typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (currentStep > 0) {
                            OutlinedButton(
                                onClick = { viewModel.prevStep() },
                                modifier = Modifier.weight(0.4f).height(48.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Previous", style = Typography.labelMedium)
                            }
                        }
                        Button(
                            onClick = { viewModel.nextStep() },
                            modifier = Modifier.weight(0.6f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EarthBrown),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (currentStep == viewModel.categories.size - 1) "Finish Check →" else "Next Step →",
                                style = Typography.labelMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        } else {
            HostCertificationView(score, viewModel) {
                navController.navigateUp()
            }
        }
    }
}

@Composable
fun PremiumChecklistItem(item: com.gramavasathi.data.model.ChecklistItem, isChecked: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) Color.White else Color.White.copy(alpha = 0.6f)
        ),
        border = BorderStroke(1.dp, if (isChecked) EarthBrown else DividerWarm)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isChecked) LeafGreen else DividerWarm),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
            
            Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                Text(
                    text = item.title,
                    style = Typography.titleLarge,
                    fontSize = 16.sp,
                    color = if (isChecked) EarthBrown else MutedBrown
                )
                Text(
                    text = item.description,
                    style = Typography.bodyMedium,
                    fontSize = 13.sp
                )
            }
            
            Surface(
                color = WarmBeige,
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "+${item.points}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = Typography.labelMedium,
                    fontSize = 10.sp,
                    color = EarthBrown
                )
            }
        }
    }
}

@Composable
fun HostCertificationView(score: Int, viewModel: HostViewModel, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Certificate Style Box
        Surface(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            color = WarmBeige,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, GoldenWheat)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_host),
                    contentDescription = null,
                    tint = EarthBrown,
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "CERTIFICATE OF READINESS",
                    style = Typography.labelMedium,
                    color = MutedBrown,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = getScoreTier(score).uppercase(),
                    style = Typography.displayLarge,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 24.dp)) {
                    CircularProgressIndicator(
                        progress = score / 100f,
                        modifier = Modifier.size(140.dp),
                        color = getScoreColor(score),
                        strokeWidth = 10.dp,
                        trackColor = Color.White
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = score.toString(), style = Typography.displayLarge, fontSize = 42.sp)
                        Text(text = "/ 100", style = Typography.labelMedium, color = MutedBrown)
                    }
                }
                
                Text(
                    text = "Based on our 5-step village standards check, your farm stay is officially recognized as a community partner.",
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Actions
        Button(
            onClick = { /* Share */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EarthBrown),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
            Text("Share Achievement", modifier = Modifier.padding(start = 12.dp), color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Continue to Platform", color = EarthBrown)
        }
    }
}
