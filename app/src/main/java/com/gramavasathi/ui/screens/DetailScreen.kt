package com.gramavasathi.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.ui.theme.*
import com.gramavasathi.ui.viewmodel.DetailViewModel
import com.gramavasathi.utils.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun HomestayDetailScreen(homestayId: String, navController: NavController, viewModel: DetailViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    
    LaunchedEffect(key1 = homestayId) {
        viewModel.fetchHomestay(homestayId)
    }

    Box(modifier = Modifier.fillMaxSize().background(CreamWhite)) {
        state.homestay?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Premium Gallery Pager
                Box(modifier = Modifier.height(360.dp)) {
                    val pagerState = rememberPagerState(pageCount = { data.image_urls.size })
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = data.image_urls[page],
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    // Gradient Overlay
                    Box(modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.2f)),
                            startY = 0f,
                            endY = 500f
                        )
                    ))

                    // Glassy Gallery Info
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1} / ${data.image_urls.size}",
                            color = Color.White,
                            style = Typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                // Main Info Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-32).dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(CreamWhite)
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = WarmBeige, shape = RoundedCornerShape(8.dp)) {
                            Text(
                                text = "SUPERHOST",
                                color = EarthBrown,
                                style = Typography.labelMedium,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = GoldenWheat, modifier = Modifier.size(18.dp))
                            Text(text = data.rating.toString(), style = Typography.titleLarge, fontSize = 16.sp, modifier = Modifier.padding(start = 4.dp))
                        }
                    }

                    Text(
                        text = data.name,
                        style = Typography.displayLarge,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    
                    Text(
                        text = "📍 ${data.village}, ${data.district}",
                        style = Typography.bodyLarge,
                        color = MutedBrown,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Quick Stats
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        DetailStatItem(icon = Icons.Default.Person, label = "${data.max_guests} Guests")
                        DetailStatItem(icon = Icons.Default.Home, label = "Farm Stay")
                        DetailStatItem(icon = Icons.Default.CheckCircle, label = "Verified")
                    }

                    Divider(modifier = Modifier.padding(vertical = 24.dp), color = DividerWarm)

                    Text(text = "The Experience", style = Typography.titleLarge)
                    Text(
                        text = data.description,
                        style = Typography.bodyLarge,
                        modifier = Modifier.padding(top = 12.dp),
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = "Activities 🌾", style = Typography.titleLarge)
                    FlowRow(
                        modifier = Modifier.padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        data.activities.forEach { activity ->
                            Surface(
                                color = WarmBeige,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, DividerWarm)
                            ) {
                                Text(
                                    text = activity,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    style = Typography.labelMedium,
                                    color = EarthBrown
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = "Amenities 🏡", style = Typography.titleLarge)
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        data.amenities.chunked(2).forEach { row ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                row.forEach { amenity ->
                                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(24.dp).background(LeafGreen.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = LeafGreen, modifier = Modifier.size(14.dp))
                                        }
                                        Text(text = amenity, style = Typography.bodyMedium, modifier = Modifier.padding(start = 12.dp))
                                    }
                                }
                                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Premium Host Card
                    Text(text = "Meet Your Host", style = Typography.titleLarge)
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, DividerWarm)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(EarthBrown), contentAlignment = Alignment.Center) {
                                    Icon(painter = painterResource(id = R.drawable.ic_host), contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                                }
                                Column(modifier = Modifier.padding(start = 16.dp)) {
                                    Text(text = data.host_name, style = Typography.titleLarge, fontSize = 18.sp)
                                    Text(text = "Community Partner", style = Typography.bodyMedium)
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Host Readiness Score", style = Typography.labelMedium, color = MutedBrown, modifier = Modifier.weight(1f))
                                Text(text = "${data.host_readiness_score}%", style = Typography.titleLarge, color = getScoreColor(data.host_readiness_score))
                            }
                            LinearProgressIndicator(
                                progress = data.host_readiness_score / 100f,
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp).height(8.dp).clip(RoundedCornerShape(4.dp)),
                                color = getScoreColor(data.host_readiness_score),
                                trackColor = DividerWarm
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        // Floating Back Button
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart).background(Color.White.copy(alpha = 0.8f), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = EarthBrown)
        }

        // Premium Sticky Bottom Bar
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            color = Color.White,
            shadowElevation = 24.dp
        ) {
            Row(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "₹${state.homestay?.price_per_night ?: 0}", style = Typography.displayLarge, fontSize = 22.sp, color = Terracotta)
                    Text(text = "Total per night", style = Typography.labelMedium, color = MutedBrown)
                }
                Button(
                    onClick = {
                        state.homestay?.let {
                            navController.navigate("booking/${it.id}/${it.name}/${it.price_per_night}")
                        }
                    },
                    modifier = Modifier.height(56.dp).padding(start = 24.dp).weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = EarthBrown),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Reserve Now", style = Typography.titleLarge, color = Color.White, fontSize = 16.sp)
                }
            }
        }

        // Loading Overlay
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GoldenWheat)
            }
        }
    }
}

@Composable
fun DetailStatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(48.dp).background(WarmBeige, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = EarthBrown, modifier = Modifier.size(20.dp))
        }
        Text(text = label, style = Typography.labelMedium, modifier = Modifier.padding(top = 8.dp), fontSize = 11.sp)
    }
}
