package com.gramavasathi.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.gramavasathi.ui.viewmodel.DetailViewModel
import com.gramavasathi.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomestayDetailScreen(homestayId: String, navController: NavController, viewModel: DetailViewModel = viewModel()) {
    val homestay by viewModel.homestay.observeAsState()
    
    LaunchedEffect(key1 = homestayId) {
        viewModel.fetchHomestay(homestayId)
    }

    Scaffold(
        bottomBar = {
            homestay?.let {
                DetailBottomBar(it) {
                    navController.navigate("booking/${it.id}/${it.name}/${it.price_per_night}")
                }
            }
        }
    ) { padding ->
        homestay?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(CreamWhite)
                    .padding(padding)
            ) {
                // Parallax-ish Image Gallery
                Box(modifier = Modifier.height(280.dp)) {
                    val pagerState = rememberPagerState(pageCount = { data.image_urls.size })
                    HorizontalPager(state = pagerState) { page ->
                        AsyncImage(
                            model = data.image_urls[page],
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    // Back Button
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    
                    // Page Indicator
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "${pagerState.currentPage + 1} / ${data.image_urls.size}",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                // Info Section
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = data.name,
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 24.sp
                    )
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MutedBrown, modifier = Modifier.size(16.dp))
                        Text(
                            text = "${data.village}, ${data.district}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 14.sp,
                            color = MutedBrown,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    
                    Text(
                        text = "₹${data.price_per_night} / night · Max ${data.max_guests} guests",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "★ ${data.rating} (${data.review_count} reviews)", color = MutedBrown, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        if (data.is_verified) {
                            Surface(color = WarmBeige, shape = RoundedCornerShape(50.dp)) {
                                Text(
                                    text = "Verified Host ✓",
                                    color = LeafGreen,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 16.dp), color = DividerWarm)
                    
                    Text(text = "About this home", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp)
                    Text(
                        text = data.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp),
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "What You'll Do 🌾", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp)
                    FlowRow(modifier = Modifier.padding(top = 8.dp), mainAxisSpacing = 8.dp, crossAxisSpacing = 4.dp) {
                        data.activities.forEach { activity ->
                            Surface(color = WarmBeige, shape = RoundedCornerShape(50.dp)) {
                                Text(
                                    text = activity,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 13.sp,
                                    color = EarthBrown
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "What's Included 🏡", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp)
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        data.amenities.chunked(2).forEach { row ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                row.forEach { amenity ->
                                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LeafGreen, modifier = Modifier.size(18.dp))
                                        Text(text = amenity, fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
                                    }
                                }
                                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 24.dp), color = DividerWarm)
                    
                    // Host Info
                    Text(text = "Your Host", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp)
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmBeige),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = EarthBrown) {
                                    Icon(painter = painterResource(id = R.drawable.ic_host), contentDescription = null, tint = CreamWhite, modifier = Modifier.padding(8.dp))
                                }
                                Text(text = data.host_name, style = MaterialTheme.typography.headlineMedium, fontSize = 16.sp, modifier = Modifier.padding(start = 16.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Host Readiness Score", color = MutedBrown, fontSize = 12.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LinearProgressIndicator(
                                    progress = data.host_readiness_score / 100f,
                                    modifier = Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)),
                                    color = getScoreColor(data.host_readiness_score),
                                    trackColor = DividerWarm
                                )
                                Text(text = "${data.host_readiness_score}/100", modifier = Modifier.padding(start = 12.dp), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun DetailBottomBar(homestay: Homestay, onBookClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        Button(
            onClick = onBookClick,
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("₹${homestay.price_per_night}/night · Book Your Stay →", style = MaterialTheme.typography.displayLarge, fontSize = 16.sp, color = Color.White)
        }
    }
}
