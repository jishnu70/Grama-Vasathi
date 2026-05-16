package com.gramavasathi.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gramavasathi.R
import com.gramavasathi.Screen
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.ui.viewmodel.HomeViewModel
import com.gramavasathi.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val homestays by viewModel.homestays.observeAsState(emptyList())
    val featured by viewModel.featuredHomestays.observeAsState(emptyList())
    val selectedActivities by viewModel.selectedActivities.observeAsState(emptySet())

    val activitiesList = listOf(
        "🐄 Cow Milking", "🌾 Field Plowing", "🍳 Local Cooking", "🐦 Bird Watching",
        "🌅 Sunrise Trek", "🎣 Fishing", "🌿 Herb Garden", "🏞️ Nature Walk"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamWhite)
    ) {
        // Custom Toolbar
        item {
            HomeToolbar(onSearchClick = { navController.navigate(Screen.Explore.route) })
        }

        // Hero Section
        item {
            if (featured.isNotEmpty()) {
                FeaturedPager(featured) { id ->
                    navController.navigate("detail/$id")
                }
            }
        }

        // Activity Filter
        item {
            Text(
                text = "What's your vibe?",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(activitiesList) { activity ->
                    FilterChip(
                        selected = selectedActivities.contains(activity),
                        onClick = { viewModel.toggleActivity(activity) },
                        label = { Text(activity) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EarthBrown,
                            selectedLabelColor = CreamWhite,
                            containerColor = WarmBeige,
                            labelColor = EarthBrown
                        )
                    )
                }
            }
        }

        // Explore Header
        item {
            Text(
                text = "Where will you wake up?",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 16.dp),
                fontSize = 20.sp
            )
        }

        // Homestay List
        items(homestays) { homestay ->
            HomestayCard(homestay) {
                navController.navigate("detail/${homestay.id}")
            }
        }

        // CTA Card
        item {
            HostCTACard { navController.navigate(Screen.Host.route) }
        }

        // Cultural Guide Banner
        item {
            CulturalBanner { navController.navigate(Screen.Guide.route) }
        }
    }
}

@Composable
fun HomeToolbar(onSearchClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_host),
            contentDescription = null,
            tint = LeafGreen,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = "Grama-Vasathi",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        )
        IconButton(onClick = onSearchClick) {
            Icon(Icons.Default.Search, contentDescription = null, tint = EarthBrown)
        }
        IconButton(onClick = { /* Notifications */ }) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = EarthBrown)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedPager(featured: List<Homestay>, onHomestayClick: (String) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { featured.size })
    
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(4000)
            val nextPage = (pagerState.currentPage + 1) % featured.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = Modifier.height(220.dp)) {
        HorizontalPager(state = pagerState) { page ->
            val homestay = featured[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onHomestayClick(homestay.id) }
            ) {
                AsyncImage(
                    model = homestay.image_urls.firstOrNull(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 100f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = homestay.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "${homestay.village}, ${homestay.district}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = CreamWhite,
                        fontSize = 14.sp
                    )
                }
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    color = GoldenWheat,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "₹${homestay.price_per_night}/night",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = EarthBrown
                    )
                }
            }
        }
        
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(featured.size) { index ->
                val color = if (pagerState.currentPage == index) Terracotta else DividerWarm
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun HomestayCard(homestay: Homestay, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(160.dp)) {
                AsyncImage(
                    model = homestay.image_urls.firstOrNull(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (homestay.is_verified) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        color = LeafGreen,
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = "Verified ✓",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = homestay.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 18.sp
                )
                Text(
                    text = "${homestay.village}, ${homestay.district}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 13.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    homestay.activities.take(3).forEach { activity ->
                        Surface(
                            color = Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DividerWarm),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = activity,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = EarthBrown
                            )
                        }
                    }
                }
                
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = DividerWarm)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "★ ${homestay.rating} · ${homestay.review_count} reviews",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 13.sp,
                        color = EarthBrown
                    )
                    Text(
                        text = "₹${homestay.price_per_night}/night",
                        style = MaterialTheme.typography.bodyLarge,
                        color = GoldenWheat,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LinearProgressIndicator(
                    progress = homestay.host_readiness_score / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = getScoreColor(homestay.host_readiness_score),
                    trackColor = DividerWarm
                )
                Text(
                    text = "Host Score: ${homestay.host_readiness_score}/100",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun HostCTACard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFC1440E), Color(0xFFE65100))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Have a spare room?",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontSize = 22.sp
                )
                Text(
                    text = "Earn ₹15,000+ per month hosting city guests",
                    style = MaterialTheme.typography.bodyLarge,
                    color = CreamWhite,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = CreamWhite),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text("Start Host Training →", color = EarthBrown)
                }
            }
        }
    }
}

@Composable
fun CulturalBanner(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .background(WarmBeige)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_host),
            contentDescription = null,
            tint = LeafGreen,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "First time in a village? Read Guide →",
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun getScoreColor(score: Int): Color {
    return when (score) {
        in 0..39 -> Color.Red
        in 40..69 -> Color(0xFFFFA500)
        in 70..89 -> GoldenWheat
        else -> LeafGreen
    }
}
