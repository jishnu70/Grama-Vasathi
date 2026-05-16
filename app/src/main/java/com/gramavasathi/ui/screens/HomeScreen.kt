package com.gramavasathi.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gramavasathi.utils.*
import coil.compose.AsyncImage
import com.gramavasathi.R
import com.gramavasathi.Screen
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.ui.theme.*
import com.gramavasathi.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
fun lerp(start: Float, stop: Float, fraction: Float): Float = start + (stop - start) * fraction

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    
    val activitiesList = listOf(
        "🐄 Cow Milking", "🌾 Field Plowing", "🍳 Local Cooking", "🐦 Bird Watching",
        "🌅 Sunrise Trek", "🎣 Fishing", "🌿 Herb Garden", "🏞️ Nature Walk"
    )

    Box(modifier = Modifier.fillMaxSize().background(CreamWhite)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Hero Space (Padding for Glassy Toolbar)
            item { Spacer(modifier = Modifier.height(72.dp)) }

            // Dynamic Welcome
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(
                        text = "Namaskara,",
                        style = Typography.bodyLarge,
                        color = MutedBrown
                    )
                    Text(
                        text = "Find your rural escape",
                        style = Typography.displayLarge,
                        fontSize = 28.sp
                    )
                }
            }

            // Premium Featured Pager
            item {
                if (state.featured.isNotEmpty()) {
                    FeaturedPager(state.featured) { id ->
                        navController.navigate("detail/$id")
                    }
                }
            }

            // Experience Filter
            item {
                Text(
                    text = "Pick your experience",
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(start = 20.dp, top = 32.dp, bottom = 12.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(activitiesList) { activity ->
                        val isSelected = state.selectedActivities.contains(activity)
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { viewModel.toggleActivity(activity) }
                                .animateContentSize(),
                            color = if (isSelected) EarthBrown else WarmBeige,
                            shadowElevation = if (isSelected) 4.dp else 0.dp
                        ) {
                            Text(
                                text = activity,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                style = Typography.labelMedium,
                                color = if (isSelected) CreamWhite else EarthBrown
                            )
                        }
                    }
                }
            }

            // Homestays Grid/List
            item {
                Text(
                    text = "Authentic Farm Stays",
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(start = 20.dp, top = 32.dp, bottom = 16.dp)
                )
            }

            items(state.homestays) { homestay ->
                PremiumHomestayCard(
                    homestay = homestay,
                    isWishlisted = state.wishlist.contains(homestay.id),
                    onWishlistToggle = { viewModel.toggleWishlist(homestay.id) },
                    onClick = { navController.navigate("detail/${homestay.id}") }
                )
            }

            // Bottom CTA
            item {
                HostPremiumCTA { navController.navigate(Screen.Host.route) }
            }
        }

        // Glassmorphism Toolbar
        GlassyTopBar(onSearchClick = { navController.navigate(Screen.Explore.route) })
    }
}

@Composable
fun GlassyTopBar(onSearchClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        color = Color.White.copy(alpha = 0.7f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(EarthBrown),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_host),
                    contentDescription = null,
                    tint = CreamWhite,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Grama-Vasathi",
                style = Typography.headlineMedium,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 12.dp).weight(1f)
            )
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.background(WarmBeige, CircleShape)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = EarthBrown)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedPager(featured: List<Homestay>, onHomestayClick: (String) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { featured.size })
    
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(5000)
            if (pagerState.pageCount > 0) {
                val next = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(next)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 32.dp),
        pageSpacing = 16.dp,
        modifier = Modifier.height(300.dp)
    ) { page ->
        val homestay = featured[page]
        val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
        
        Card(
            modifier = Modifier
                .graphicsLayer {
                    val scale = lerp(0.85f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                    scaleX = scale
                    scaleY = scale
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                }
                .fillMaxSize()
                .clickable { onHomestayClick(homestay.id) },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box {
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
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 300f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Surface(
                        color = GoldenWheat,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "TOP RATED",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = Typography.labelMedium,
                            fontSize = 10.sp,
                            color = EarthBrown
                        )
                    }
                    Text(
                        text = homestay.name,
                        style = Typography.headlineMedium,
                        color = Color.White,
                        fontSize = 22.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "📍 ${homestay.village}",
                        style = Typography.bodyLarge,
                        color = CreamWhite,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumHomestayCard(
    homestay: Homestay,
    isWishlisted: Boolean,
    onWishlistToggle: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.height(200.dp)) {
                AsyncImage(
                    model = homestay.image_urls.firstOrNull(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Wishlist Button
                IconButton(
                    onClick = onWishlistToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isWishlisted) Terracotta else EarthBrown
                    )
                }

                // Verified Badge
                if (homestay.is_verified) {
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                        color = LeafGreen,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Text(text = "VERIFIED", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = homestay.name,
                        style = Typography.titleLarge,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = GoldenWheat, modifier = Modifier.size(16.dp))
                        Text(text = homestay.rating.toString(), style = Typography.labelMedium, modifier = Modifier.padding(start = 4.dp))
                    }
                }
                
                Text(
                    text = "${homestay.village}, ${homestay.district}",
                    style = Typography.bodyMedium,
                    color = MutedBrown
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Host Score Bar
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        LinearProgressIndicator(
                            progress = homestay.host_readiness_score / 100f,
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = getScoreColor(homestay.host_readiness_score),
                            trackColor = DividerWarm
                        )
                        Text(
                            text = "Host Readiness: ${homestay.host_readiness_score}%",
                            style = Typography.labelMedium,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "₹${homestay.price_per_night}", style = Typography.titleLarge, color = Terracotta)
                        Text(text = "per night", style = Typography.labelMedium, fontSize = 10.sp, color = MutedBrown)
                    }
                }
            }
        }
    }
}

@Composable
fun HostPremiumCTA(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(NatureGradient))
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_host),
            contentDescription = null,
            modifier = Modifier.size(140.dp).align(Alignment.CenterEnd).offset(x = 40.dp).blur(2.dp).graphicsLayer { alpha = 0.2f },
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
        )
        Column(modifier = Modifier.padding(24.dp).align(Alignment.CenterStart)) {
            Text(
                text = "Open Your Doors",
                style = Typography.headlineMedium,
                color = Color.White,
                fontSize = 20.sp
            )
            Text(
                text = "Join our community of hosts\nand share your village life.",
                style = Typography.bodyLarge,
                color = CreamWhite,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Get Certified →",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = Typography.labelMedium,
                    color = LeafGreen
                )
            }
        }
    }
}
