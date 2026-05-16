package com.gramavasathi.ui.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramavasathi.BuildConfig
import com.gramavasathi.ui.components.HomestayCard
import com.gramavasathi.ui.components.NetworkImage
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.DividerWarm
import com.gramavasathi.ui.theme.EarthBrown
import com.gramavasathi.ui.theme.WarmBeige
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onExplore: () -> Unit,
    onHost: () -> Unit,
    onGuide: () -> Unit,
    onDetails: (String) -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val homestays by vm.all.collectAsStateWithLifecycle()
    val selected by vm.selectedActivity.collectAsStateWithLifecycle()

    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(Unit) { vm.load() }
    val featured = homestays.take(3)
    val pagerState = rememberPagerState { featured.size.coerceAtLeast(1) }

    LaunchedEffect(featured.size) {
        while (true) {
            delay(4000)
            if (featured.isNotEmpty()) pagerState.animateScrollToPage((pagerState.currentPage + 1) % featured.size)
        }
    }

    LazyColumn(
        modifier = Modifier.background(CreamWhite).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("🌿 Grama-Vasathi", style = MaterialTheme.typography.headlineMedium, color = EarthBrown, modifier = Modifier.clickable(enabled = BuildConfig.DEBUG) {
                    vm.seed { ok ->
                        Toast.makeText(context, if (ok) "Seeded successfully" else "Seeding failed", Toast.LENGTH_SHORT).show()
                    }
                })
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Rounded.Search, null, modifier = Modifier.clickable { onExplore() })
                    Icon(Icons.Rounded.Notifications, null)
                }
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(DividerWarm))
        }

        item {
            if (featured.isNotEmpty()) {
                HorizontalPager(pagerState, modifier = Modifier.fillMaxWidth().height(200.dp)) { idx ->
                    val h = featured[idx]
                    Box {
                        NetworkImage(h.image_urls.firstOrNull().orEmpty(), Modifier.fillMaxWidth().height(200.dp).clip(MaterialTheme.shapes.large))
                        Column(Modifier.align(Alignment.BottomStart).padding(12.dp)) {
                            Text(h.name, color = androidx.compose.ui.graphics.Color.White)
                            Text("${h.village}, ${h.district}", color = androidx.compose.ui.graphics.Color.White)
                        }
                    }
                }
            }
        }

        item {
            Text("What's your vibe?", style = MaterialTheme.typography.titleLarge, color = EarthBrown)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.activities) { activity ->
                    Card(modifier = Modifier.clickable { vm.setActivity(activity.name) }) {
                        Text(
                            "${activity.emoji} ${activity.name}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            color = if (selected == activity.name) androidx.compose.ui.graphics.Color.White else EarthBrown
                        )
                    }
                }
            }
        }

        item { Text("Where will you wake up?", style = MaterialTheme.typography.titleLarge, color = EarthBrown) }
        items(vm.filtered()) { h -> HomestayCard(h = h, onClick = { onDetails(h.id) }) }

        item {
            Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                Column(Modifier.background(WarmBeige).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Have a spare room?", style = MaterialTheme.typography.titleLarge, color = EarthBrown)
                    Text("Earn ₹15,000+ per month hosting city guests")
                    Button(onClick = onHost) { Text("Start Host Training →") }
                }
            }
        }

        item {
            Card(Modifier.fillMaxWidth().clickable { onGuide() }) {
                Text("🌿 First time in a village? Read Guide →", Modifier.padding(16.dp), color = EarthBrown)
            }
        }
    }
}
