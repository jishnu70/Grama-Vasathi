package com.gramavasathi.ui.detail

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramavasathi.data.model.Review
import com.gramavasathi.ui.components.AppTopBar
import com.gramavasathi.ui.components.NetworkImage
import com.gramavasathi.ui.components.ScoreBar
import com.gramavasathi.ui.components.SectionHeader
import com.gramavasathi.ui.components.tierText
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.DividerWarm
import com.gramavasathi.ui.theme.EarthBrown
import com.gramavasathi.ui.theme.GoldenWheat
import com.gramavasathi.ui.theme.LeafGreen
import com.gramavasathi.ui.theme.MutedBrown
import com.gramavasathi.ui.theme.Terracotta
import com.gramavasathi.ui.theme.WarmBeige

private val sampleReviews = listOf(
    Review("Priya S.", 5, "Absolutely magical experience! Woke up to birdsong and fresh filter coffee. Raju bhaiya's farm stories are priceless.", "March 2024"),
    Review("Arun K.", 4, "Simple, homely, and healing. The food was the best part — pure village cooking with love.", "February 2024"),
    Review("Meena T.", 5, "Took my parents here and they couldn't stop smiling. Felt like visiting our ancestral village.", "January 2024")
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun HomestayDetailScreen(
    id: String,
    onBack: () -> Unit,
    onBook: (String) -> Unit,
    vm: DetailViewModel = viewModel()
) {
    val h by vm.homestay.collectAsStateWithLifecycle()
    val isFavorite by vm.isFavorite.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(id) { vm.load(id) }

    Column(Modifier.fillMaxSize().background(CreamWhite)) {
        AppTopBar(
            title = h?.name ?: "Details",
            onBack = onBack,
            actions = {
                if (h != null) {
                    IconButton(onClick = { vm.toggleFavorite() }) {
                        Icon(
                            if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Terracotta else EarthBrown
                        )
                    }
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT,
                                "Check out ${h!!.name} in ${h!!.village} — ₹${h!!.price_per_night}/night on Grama-Vasathi! 🌿")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Homestay"))
                    }) {
                        Icon(Icons.Rounded.Share, contentDescription = "Share", tint = EarthBrown)
                    }
                }
            }
        )

        if (h == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = EarthBrown)
            }
            return@Column
        }

        val homestay = h!!
        val pagerState = rememberPagerState { homestay.image_urls.size.coerceAtLeast(1) }

        LazyColumn(Modifier.weight(1f)) {
            // ── Image Gallery ──────────────────────────────────────────────
            item {
                Box(Modifier.fillMaxWidth().height(260.dp)) {
                    HorizontalPager(
                        pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { idx ->
                        NetworkImage(
                            homestay.image_urls.getOrElse(idx) { "" },
                            Modifier.fillMaxSize()
                        )
                    }
                    // Image counter badge
                    Text(
                        "${pagerState.currentPage + 1} / ${homestay.image_urls.size.coerceAtLeast(1)}",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .background(Color(0x88000000), RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    // Thumbnail dots
                    Row(
                        Modifier.align(Alignment.BottomCenter).padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        repeat(homestay.image_urls.size.coerceAtLeast(1)) { i ->
                            Box(
                                Modifier
                                    .size(if (pagerState.currentPage == i) 9.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(if (pagerState.currentPage == i) GoldenWheat else Color.White.copy(0.7f))
                            )
                        }
                    }
                }
            }

            // ── Main Info ──────────────────────────────────────────────────
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(homestay.name, style = MaterialTheme.typography.headlineMedium, color = EarthBrown, fontWeight = FontWeight.Bold)
                    Text("📍 ${homestay.village}, ${homestay.district}", color = MutedBrown, fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("★", color = GoldenWheat, fontSize = 18.sp)
                        Text("${homestay.rating}", fontWeight = FontWeight.SemiBold, color = EarthBrown)
                        Text("· ${homestay.review_count} reviews", color = MutedBrown, fontSize = 13.sp)
                        Spacer(Modifier.weight(1f))
                        Text("₹${homestay.price_per_night}/night", fontWeight = FontWeight.Bold, color = EarthBrown, fontSize = 16.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Max ${homestay.max_guests} guests · ${homestay.state}", color = MutedBrown, fontSize = 13.sp)
                        if (homestay.is_verified) {
                            Text(
                                "✓ Verified",
                                color = Color.White,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .background(LeafGreen, RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    ScoreBar(homestay.host_readiness_score)
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(DividerWarm))
            }

            // ── Description ────────────────────────────────────────────────
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionHeader("About This Stay 🌿")
                    Text(homestay.description, color = MutedBrown, fontSize = 14.sp, lineHeight = 22.sp)
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(DividerWarm))
            }

            // ── Activities ─────────────────────────────────────────────────
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("What You'll Do 🌾")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        homestay.activities.forEach { act ->
                            Text(
                                act,
                                color = EarthBrown,
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .background(WarmBeige, RoundedCornerShape(50))
                                    .border(1.dp, DividerWarm, RoundedCornerShape(50))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(DividerWarm))
            }

            // ── Amenities ──────────────────────────────────────────────────
            item {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("What's Included 🏡")
                }
            }
            items(homestay.amenities.chunked(2)) { row ->
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { amenity ->
                        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("✓", color = LeafGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(amenity, color = EarthBrown, fontSize = 13.sp)
                        }
                    }
                    if (row.size < 2) Spacer(Modifier.weight(1f))
                }
            }

            // ── Host bio ───────────────────────────────────────────────────
            item {
                Box(Modifier.fillMaxWidth().height(1.dp).background(DividerWarm).padding(top = 8.dp))
                Card(
                    Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmBeige),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SectionHeader("About the Host 👨‍🌾")
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                Modifier.size(48.dp).clip(CircleShape).background(EarthBrown),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    homestay.host_name.firstOrNull()?.toString() ?: "H",
                                    color = CreamWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Column {
                                Text(homestay.host_name, fontWeight = FontWeight.SemiBold, color = EarthBrown)
                                Text("Host since 2022", fontSize = 12.sp, color = MutedBrown)
                            }
                        }
                        Text(
                            "Warm village host focused on comfort, authentic meals, and sharing the real rural experience with every guest.",
                            color = MutedBrown,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )
                        ScoreBar(homestay.host_readiness_score)
                        Text(tierText(homestay.host_readiness_score), color = MutedBrown, fontSize = 12.sp)
                    }
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(DividerWarm))
            }

            // ── Reviews ────────────────────────────────────────────────────
            item {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    SectionHeader("Guest Reviews ⭐")
                    Text("${homestay.rating} · ${homestay.review_count} reviews", color = MutedBrown, fontSize = 13.sp)
                }
            }
            items(sampleReviews) { review ->
                Card(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmBeige),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(review.author, fontWeight = FontWeight.SemiBold, color = EarthBrown)
                            Text("★".repeat(review.rating), color = GoldenWheat, fontSize = 13.sp)
                        }
                        Text(review.comment, color = MutedBrown, fontSize = 13.sp, lineHeight = 20.sp)
                        Text(review.date, color = MutedBrown.copy(0.7f), fontSize = 11.sp)
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }

        // ── Sticky Book Now bar ────────────────────────────────────────────
        Box(
            Modifier.fillMaxWidth().background(CreamWhite).padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = { onBook(h!!.id) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta, contentColor = Color.White)
            ) {
                Text("₹${h?.price_per_night}/night · Book Your Stay →", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
