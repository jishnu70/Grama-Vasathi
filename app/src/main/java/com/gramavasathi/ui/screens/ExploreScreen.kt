package com.gramavasathi.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gramavasathi.R
import com.gramavasathi.Screen
import com.gramavasathi.ui.viewmodel.ExploreViewModel
import com.gramavasathi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController, viewModel: ExploreViewModel = viewModel()) {
    val homestays by viewModel.homestays.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var isFiltersExpanded by remember { mutableStateOf(false) }
    
    val activitiesList = listOf(
        "🐄 Cow Milking", "🌾 Field Plowing", "🍳 Local Cooking", "🐦 Bird Watching",
        "🌅 Sunrise Trek", "🎣 Fishing", "🌿 Herb Garden", "🏞️ Nature Walk"
    )
    
    var selectedActivities by remember { mutableStateOf(setOf<String>()) }
    var priceRange by remember { mutableStateOf(0f..3000f) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Navigate to Map Screen or show map overlay */ },
                containerColor = EarthBrown,
                contentColor = CreamWhite
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = "Map")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamWhite)
                .padding(padding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.onSearchQueryChanged(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search by village, district, or activity...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldenWheat,
                    unfocusedBorderColor = DividerWarm
                )
            )

            // Filters Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isFiltersExpanded = !isFiltersExpanded }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isFiltersExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = EarthBrown
                )
            }

            AnimatedVisibility(visible = isFiltersExpanded) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Activities", fontWeight = FontWeight.Bold, color = EarthBrown)
                    FlowRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 4.dp
                    ) {
                        activitiesList.forEach { activity ->
                            FilterChip(
                                selected = selectedActivities.contains(activity),
                                onClick = {
                                    selectedActivities = if (selectedActivities.contains(activity)) {
                                        selectedActivities - activity
                                    } else {
                                        selectedActivities + activity
                                    }
                                },
                                label = { Text(activity, fontSize = 12.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Price Range", fontWeight = FontWeight.Bold, color = EarthBrown)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "₹${priceRange.start.toInt()}", fontSize = 12.sp)
                        Text(text = "₹${priceRange.endInclusive.toInt()}", fontSize = 12.sp)
                    }
                    RangeSlider(
                        value = priceRange,
                        onValueChange = { priceRange = it },
                        valueRange = 0f..3000f,
                        colors = SliderDefaults.colors(
                            thumbColor = Terracotta,
                            activeTrackColor = GoldenWheat,
                            inactiveTrackColor = DividerWarm
                        )
                    )

                    Button(
                        onClick = {
                            viewModel.setFilters(selectedActivities, priceRange.start, priceRange.endInclusive)
                            isFiltersExpanded = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text("Apply Filters")
                    }
                }
            }

            // Results Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${homestays.size} farm stays found",
                    color = MutedBrown,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sort: Rating ↓",
                    fontWeight = FontWeight.Bold,
                    color = EarthBrown,
                    fontSize = 14.sp
                )
            }

            // Results List
            if (homestays.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(homestays) { homestay ->
                        HomestayCard(homestay) {
                            navController.navigate("detail/${homestay.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_host),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = DividerWarm
        )
        Text(
            text = "No farm stays match your search",
            color = MutedBrown,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    mainAxisSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    crossAxisSpacing: androidx.compose.ui.unit.Dp = 0.dp,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.layout.Layout(content = content, modifier = modifier) { measurables, constraints ->
        val placeholders = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }
        val spacing = mainAxisSpacing.roundToPx()
        val rowSpacing = crossAxisSpacing.roundToPx()
        
        val rows = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        var currentRow = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentRowWidth = 0
        
        placeholders.forEach { placeable ->
            if (currentRowWidth + placeable.width + spacing > constraints.maxWidth && currentRow.isNotEmpty()) {
                rows.add(currentRow)
                currentRow = mutableListOf()
                currentRowWidth = 0
            }
            currentRow.add(placeable)
            currentRowWidth += placeable.width + spacing
        }
        rows.add(currentRow)
        
        val totalHeight = rows.sumOf { it.maxOf { p -> p.height } } + (rows.size - 1) * rowSpacing
        
        layout(constraints.maxWidth, totalHeight) {
            var y = 0
            rows.forEach { row ->
                var x = 0
                val rowHeight = row.maxOf { it.height }
                row.forEach { placeable ->
                    placeable.place(x, y)
                    x += placeable.width + spacing
                }
                y += rowHeight + rowSpacing
            }
        }
    }
}
