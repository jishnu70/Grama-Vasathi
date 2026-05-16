package com.gramavasathi.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramavasathi.ui.components.HomestayCard
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.EarthBrown

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(
    onDetails: (String) -> Unit,
    onMap: () -> Unit,
    vm: ExploreViewModel = viewModel()
) {
    val q by vm.query.collectAsStateWithLifecycle()
    val selected by vm.selectedActivities.collectAsStateWithLifecycle()
    val min by vm.priceMin.collectAsStateWithLifecycle()
    val max by vm.priceMax.collectAsStateWithLifecycle()
    val minScore by vm.minScore.collectAsStateWithLifecycle()
    val sort by vm.sort.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(true) }
    var sortExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.load() }
    val filtered = vm.filtered()

    Column(Modifier.background(CreamWhite)) {
        LazyColumn(Modifier.weight(1f).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                OutlinedTextField(
                    value = q,
                    onValueChange = vm::onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Search by village, district, or activity...") }
                )
            }

            item {
                Text("Filters ${if (expanded) "▲" else "▼"}", modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), color = EarthBrown)
                if (expanded) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            vm.activityList.forEach { a ->
                                AssistChip(onClick = { vm.toggleActivity(a) }, label = { Text(a) })
                            }
                        }
                        Text("₹${min.toInt()} – ₹${max.toInt()}/night")
                        RangeSlider(value = min..max, valueRange = 500f..3000f, onValueChange = { vm.setPrice(it.start, it.endInclusive) })
                        Text("Min Host Score: ${minScore.toInt()}")
                        Slider(value = minScore, onValueChange = vm::setMinScore, valueRange = 0f..100f)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { expanded = false }) { Text("Apply Filters") }
                            Button(onClick = vm::clearAll) { Text("Clear All") }
                        }
                        Text("Selected: ${selected.joinToString()}")
                    }
                }
            }

            item {
                Text("${filtered.size} farm stays found", color = EarthBrown)
                Text("Sort: $sort", modifier = Modifier.padding(top = 4.dp))
                Button(onClick = { sortExpanded = true }) { Text("Change Sort") }
                DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                    listOf("Rating", "Price: Low to High", "Price: High to Low", "Host Score").forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = { vm.setSort(it); sortExpanded = false })
                    }
                }
            }

            if (filtered.isEmpty()) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(30.dp)) {
                        Text("🌾")
                        Text("No farm stays match your search", color = Color.Gray)
                    }
                }
            }
            items(filtered) { h -> HomestayCard(h = h, onClick = { onDetails(h.id) }) }
        }

        FloatingActionButton(onClick = onMap, modifier = Modifier.align(Alignment.End).padding(16.dp)) {
            androidx.compose.material3.Icon(Icons.Rounded.Map, null)
        }
    }
}
