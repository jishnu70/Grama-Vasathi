package com.gramavasathi.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.gramavasathi.ui.theme.*
import com.gramavasathi.ui.viewmodel.ExploreViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(navController: NavController, viewModel: ExploreViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    var isFiltersExpanded by remember { mutableStateOf(false) }
    
    val activitiesList = listOf(
        "🐄 Cow Milking", "🌾 Field Plowing", "🍳 Local Cooking", "🐦 Bird Watching",
        "🌅 Sunrise Trek", "🎣 Fishing", "🌿 Herb Garden", "🏞️ Nature Walk"
    )

    Box(modifier = Modifier.fillMaxSize().background(CreamWhite)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(text = "Explore", style = Typography.displayLarge, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                // Premium Search Bar
                OutlinedTextField(
                    value = state.query,
                    onValueChange = { viewModel.onQueryChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search villages or farm activities...", style = Typography.bodyMedium) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MutedBrown) },
                    trailingIcon = {
                        IconButton(onClick = { isFiltersExpanded = !isFiltersExpanded }) {
                            Icon(
                                imageVector = if (isFiltersExpanded) Icons.Default.Close else Icons.Default.Tune,
                                contentDescription = null,
                                tint = if (isFiltersExpanded) Terracotta else EarthBrown
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldenWheat,
                        unfocusedBorderColor = DividerWarm,
                        focusedContainerColor = WarmBeige,
                        unfocusedContainerColor = WarmBeige
                    )
                )
            }

            // Expandable Filters
            AnimatedVisibility(visible = isFiltersExpanded) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Activities", style = Typography.labelMedium, color = EarthBrown)
                        FlowRow(
                            modifier = Modifier.padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            activitiesList.forEach { activity ->
                                val isSelected = state.selectedActivities.contains(activity)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        val next = if (isSelected) state.selectedActivities - activity else state.selectedActivities + activity
                                        viewModel.updateFilters(next, state.priceRange)
                                    },
                                    label = { Text(activity, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = EarthBrown,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Price Range", style = Typography.labelMedium, modifier = Modifier.weight(1f))
                            Text(text = "₹${state.priceRange.start.toInt()} - ₹${state.priceRange.endInclusive.toInt()}", style = Typography.bodyMedium)
                        }
                        RangeSlider(
                            value = state.priceRange,
                            onValueChange = { viewModel.updateFilters(state.selectedActivities, it) },
                            valueRange = 0f..5000f,
                            colors = SliderDefaults.colors(
                                thumbColor = Terracotta,
                                activeTrackColor = GoldenWheat,
                                inactiveTrackColor = DividerWarm
                            )
                        )
                    }
                }
            }

            // Results List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${state.results.size} farm stays available",
                            style = Typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(color = WarmBeige, shape = RoundedCornerShape(8.dp)) {
                            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Sort by: Top Rated", fontSize = 11.sp, color = EarthBrown)
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(14.dp), tint = EarthBrown)
                            }
                        }
                    }
                }

                if (state.results.isEmpty() && !state.isLoading) {
                    item { ExploreEmptyState() }
                }

                items(state.results) { homestay ->
                    PremiumHomestayCard(
                        homestay = homestay,
                        isWishlisted = false, // Connect to HomeViewModel wishlist if needed
                        onWishlistToggle = { },
                        onClick = { navController.navigate("detail/${homestay.id}") }
                    )
                }
            }
        }

        // Map FAB
        ExtendedFloatingActionButton(
            onClick = { /* Navigate to Map View */ },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp),
            containerColor = EarthBrown,
            contentColor = Color.White,
            shape = RoundedCornerShape(50.dp)
        ) {
            Icon(Icons.Default.Map, contentDescription = null)
            Text(text = "Map View", modifier = Modifier.padding(start = 8.dp), style = Typography.labelMedium)
        }
    }
}

@Composable
fun ExploreEmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(120.dp).background(WarmBeige, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_explore),
                contentDescription = null,
                tint = MutedBrown,
                modifier = Modifier.size(64.dp)
            )
        }
        Text(
            text = "No farm stays match your criteria",
            style = Typography.titleLarge,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = "Try adjusting your filters or search term.",
            style = Typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = { /* Reset filters logic */ }) {
            Text(text = "Clear all filters", color = Terracotta, fontWeight = FontWeight.Bold)
        }
    }
}
