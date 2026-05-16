package com.gramavasathi.ui.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramavasathi.BuildConfig
import com.gramavasathi.ui.components.AppTopBar
import com.gramavasathi.ui.components.HomestayCard
import com.gramavasathi.ui.components.HomestayCardSkeleton
import com.gramavasathi.ui.components.NetworkImage
import com.gramavasathi.ui.components.SectionHeader
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.DividerWarm
import com.gramavasathi.ui.theme.EarthBrown
import com.gramavasathi.ui.theme.GoldenWheat
import com.gramavasathi.ui.theme.LeafGreen
import com.gramavasathi.ui.theme.MutedBrown
import com.gramavasathi.ui.theme.Terracotta
import com.gramavasathi.ui.theme.WarmBeige
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onExplore: () -> Unit,
    onHost: () -> Unit,
    onGuide: () -> Unit,
    onDetails: (String) -> Unit,
    onBookings: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val homestays by vm.all.collectAsStateWithLifecycle()
    val selected by vm.selectedActivity.collectAsStateWithLifecycle()
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val favorites by vm.favorites.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) { vm.load() }

    val featured = homestays.take(3)
    val pagerState = rememberPagerState { featured.size.coerceAtLeast(1) }

    LaunchedEffect(featured.size) {
        while (true) {
            delay(4000)
            if (featured.isNotEmpty()) {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % featured.size)
            }
        }
    }

    Column(Modifier.fillMaxSize().background(CreamWhite)) {
        AppTopBar(
            title = "🌿 Grama-Vasathi",
            onBack = null,
            actions = {
                Icon(
                    Icons.Rounded.Search, null,
                    tint = EarthBrown,
                    modifier = Modifier.clickable { onExplore() }.padding(8.dp)
                )
                Icon(
                    Icons.Rounded.Bookmarks, null,
                    tint = EarthBrown,
                    modifier = Modifier.clickable { onBookings() }.padding(8.dp)
                )
                Icon(
                    Icons.Rounded.Notifications, null,
                    tint = EarthBrown,
                    modifier = Modifier
                        .clickable(enabled = BuildConfig.DEBUG) {
                            vm.seed { ok ->
                                Toast.makeText(
                                    context,
                                    if (ok) "Seeded successfully ✓" else "Seeding failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .padding(8.dp)
                )
            }
        )

        var refreshing by remember { mutableStateOf(false) }

        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = {
                refreshing = true
                vm.load { refreshing = false }
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(CreamWhite),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Hero Carousel ──────────────────────────────────────────
                item {
                    Column {
                        if (isLoading && featured.isEmpty()) {
                            Box(
                                Modifier.fillMaxWidth().height(220.dp).background(WarmBeige),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = EarthBrown)
                            }
                        } else if (featured.isNotEmpty()) {
                            HorizontalPager(
                                pagerState,
                                modifier = Modifier.fillMaxWidth().height(220.dp)
                            ) { idx ->
                                val h = featured[idx]
                                Box(Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))) {
                                    NetworkImage(h.image_urls.firstOrNull().orEmpty(), Modifier.fillMaxWidth().height(220.dp))
                                    // Gradient overlay
                                    Box(
                                        Modifier.fillMaxSize().background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color(0xCC000000)),
                                                startY = 60f
                                            )
                                        )
                                    )
                                    Column(
                                        Modifier.align(Alignment.BottomStart).padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(h.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                        Text("📍 ${h.village}, ${h.district}", color = Color.White.copy(0.85f), fontSize = 13.sp)
                                    }
                                    Text(
                                        "₹${h.price_per_night}/night",
                                        color = EarthBrown,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(12.dp)
                                            .background(GoldenWheat, RoundedCornerShape(50))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            // Pager dots
                            Row(
                                Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(featured.size) { i ->
                                    val isActive = pagerState.currentPage == i
                                    Box(
                                        Modifier
                                            .padding(horizontal = 3.dp)
                                            .size(if (isActive) 10.dp else 7.dp)
                                            .clip(CircleShape)
                                            .background(if (isActive) Terracotta else DividerWarm)
                                    )
                                }
                            }
                        }
                    }
                }

                // ── Vibe Chips ─────────────────────────────────────────────
                item {
                    Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionHeader("What's your vibe? ✨")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(vm.activities) { activity ->
                                val isSelected = selected == activity.name
                                Text(
                                    "${activity.emoji} ${activity.name}",
                                    fontSize = 13.sp,
                                    color = if (isSelected) CreamWhite else EarthBrown,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(if (isSelected) EarthBrown else WarmBeige)
                                        .clickable { vm.setActivity(activity.name) }
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // ── Homestay list ──────────────────────────────────────────
                item { SectionHeader("Where will you wake up? 🌄", Modifier.padding(horizontal = 16.dp)) }

                if (isLoading && homestays.isEmpty()) {
                    items(3) {
                        Box(Modifier.padding(horizontal = 16.dp)) {
                            HomestayCardSkeleton()
                        }
                    }
                } else {
                    items(vm.filtered()) { h ->
                        Box(Modifier.padding(horizontal = 16.dp)) {
                            HomestayCard(
                                h = h,
                                onClick = { onDetails(h.id) },
                                isFavorite = favorites.contains(h.id),
                                onFavoriteToggle = { vm.toggleFavorite(h.id) }
                            )
                        }
                    }
                }

                // ── Be a Host CTA ──────────────────────────────────────────
                item {
                    Card(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Terracotta),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("Have a spare room? 🏡", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Earn ₹15,000+ per month hosting city guests", color = Color.White.copy(0.9f), fontSize = 14.sp)
                            Button(
                                onClick = onHost,
                                colors = ButtonDefaults.buttonColors(containerColor = CreamWhite, contentColor = Terracotta),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Start Host Training →", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                // ── Cultural guide banner ──────────────────────────────────
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { onGuide() },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmBeige),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🌿", fontSize = 24.sp)
                            Column(Modifier.weight(1f)) {
                                Text("First time in a village?", fontWeight = FontWeight.SemiBold, color = EarthBrown)
                                Text("Read our cultural etiquette guide", fontSize = 13.sp, color = MutedBrown)
                            }
                            Text("→", color = EarthBrown, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}
