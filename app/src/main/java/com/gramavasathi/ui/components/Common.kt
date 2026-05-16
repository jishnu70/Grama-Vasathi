package com.gramavasathi.ui.components

import android.widget.ImageView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.DividerWarm
import com.gramavasathi.ui.theme.EarthBrown
import com.gramavasathi.ui.theme.GoldenWheat
import com.gramavasathi.ui.theme.LeafGreen
import com.gramavasathi.ui.theme.MutedBrown
import com.gramavasathi.ui.theme.TierGold
import com.gramavasathi.ui.theme.TierGreen
import com.gramavasathi.ui.theme.TierOrange
import com.gramavasathi.ui.theme.TierRed
import com.gramavasathi.ui.theme.Terracotta
import com.gramavasathi.ui.theme.WarmBeige

// ── Color/text helpers ──────────────────────────────────────────────────────

fun tierColor(score: Int): Color = when (score) {
    in 0..39 -> TierRed
    in 40..69 -> TierOrange
    in 70..89 -> TierGold
    else -> TierGreen
}

fun tierText(score: Int): String = when (score) {
    in 0..39 -> "🌱 Getting Started"
    in 40..69 -> "🌿 Almost Ready"
    in 70..89 -> "🌾 Guest Ready"
    else -> "⭐ Certified Host"
}

// ── NetworkImage via Glide ──────────────────────────────────────────────────

@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            ImageView(context).apply { scaleType = ImageView.ScaleType.CENTER_CROP }
        },
        update = {
            Glide.with(context)
                .load(url)
                .placeholder(android.R.color.darker_gray)
                .crossFade()
                .into(it)
        }
    )
}

// ── Shimmer loading skeleton ────────────────────────────────────────────────

@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by transition.animateFloat(
        initialValue = -300f, targetValue = 1200f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "x"
    )
    val brush = Brush.linearGradient(
        listOf(WarmBeige, DividerWarm, WarmBeige),
        start = Offset(shimmerX, 0f), end = Offset(shimmerX + 300f, 0f)
    )
    Box(modifier.background(brush))
}

@Composable
fun HomestayCardSkeleton() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            ShimmerBox(Modifier.fillMaxWidth().height(160.dp))
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerBox(Modifier.fillMaxWidth(0.7f).height(18.dp).clip(RoundedCornerShape(4.dp)))
                ShimmerBox(Modifier.fillMaxWidth(0.5f).height(14.dp).clip(RoundedCornerShape(4.dp)))
                ShimmerBox(Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(4.dp)))
            }
        }
    }
}

// ── Section header ──────────────────────────────────────────────────────────

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = EarthBrown,
        modifier = modifier
    )
}

// ── Pill chip ───────────────────────────────────────────────────────────────

@Composable
fun PillChip(
    label: String,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val bg = if (selected) EarthBrown else CreamWhite
    val textColor = if (selected) CreamWhite else EarthBrown
    val borderColor = if (selected) EarthBrown else DividerWarm
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(50))
            .then(if (onClick != null) Modifier.padding(0.dp) else Modifier)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = textColor, fontSize = 13.sp)
    }
}

// ── Score progress bar ──────────────────────────────────────────────────────

@Composable
fun ScoreBar(score: Int, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Host Score: $score/100", fontSize = 12.sp, color = MutedBrown)
            Text(tierText(score), fontSize = 11.sp, color = tierColor(score))
        }
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(50)),
            color = tierColor(score),
            trackColor = DividerWarm,
            strokeCap = StrokeCap.Round
        )
    }
}

// ── HomestayCard v2 ─────────────────────────────────────────────────────────

@Composable
fun HomestayCard(h: Homestay, onClick: () -> Unit, isFavorite: Boolean = false, onFavoriteToggle: (() -> Unit)? = null) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box {
                NetworkImage(
                    h.image_urls.firstOrNull().orEmpty(),
                    Modifier.fillMaxWidth().height(160.dp).clip(
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                )
                // Verified badge
                if (h.is_verified) {
                    Text(
                        "✓ Verified",
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(LeafGreen, RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                // Price badge
                Text(
                    "₹${h.price_per_night}/night",
                    color = EarthBrown,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(GoldenWheat, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
                // Favorite button
                if (onFavoriteToggle != null) {
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp).size(36.dp)
                    ) {
                        Icon(
                            if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) Terracotta else Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(h.name, style = MaterialTheme.typography.titleMedium, color = EarthBrown, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }
                Text("📍 ${h.village}, ${h.district}", fontSize = 13.sp, color = MutedBrown)
                // Activity chips (up to 3)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    h.activities.take(3).forEach { act ->
                        Text(
                            act, fontSize = 11.sp, color = EarthBrown,
                            modifier = Modifier
                                .border(1.dp, DividerWarm, RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("★", color = GoldenWheat, fontSize = 14.sp)
                        Text("${h.rating}", color = EarthBrown, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("· ${h.review_count} reviews", color = MutedBrown, fontSize = 12.sp)
                    }
                    Text("Max ${h.max_guests} guests", fontSize = 12.sp, color = MutedBrown)
                }
                ScoreBar(h.host_readiness_score)
            }
        }
    }
}