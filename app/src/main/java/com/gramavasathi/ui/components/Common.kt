package com.gramavasathi.ui.components

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.ui.theme.EarthBrown
import com.gramavasathi.ui.theme.GoldenWheat
import com.gramavasathi.ui.theme.LeafGreen
import com.gramavasathi.ui.theme.TierGold
import com.gramavasathi.ui.theme.TierGreen
import com.gramavasathi.ui.theme.TierOrange
import com.gramavasathi.ui.theme.TierRed
import com.gramavasathi.ui.theme.WarmBeige

@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            ImageView(context).apply { scaleType = ImageView.ScaleType.CENTER_CROP }
        },
        update = { Glide.with(context).load(url).into(it) }
    )
}

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

@Composable
fun HomestayCard(h: Homestay, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarmBeige),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            NetworkImage(h.image_urls.firstOrNull().orEmpty(), Modifier.fillMaxWidth())
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(h.name, style = MaterialTheme.typography.titleLarge, color = EarthBrown)
                    if (h.is_verified) {
                        Text(
                            "Verified ✓",
                            color = Color.White,
                            modifier = Modifier
                                .background(LeafGreen, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                Text("${h.village}, ${h.district}", color = EarthBrown.copy(alpha = 0.75f))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("★ ${h.rating} · ${h.review_count} reviews", color = EarthBrown)
                    Text(
                        "₹${h.price_per_night}/night",
                        color = EarthBrown,
                        modifier = Modifier.background(GoldenWheat, RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Text("Host Score: ${h.host_readiness_score}/100", color = tierColor(h.host_readiness_score))
            }
        }
    }
}
