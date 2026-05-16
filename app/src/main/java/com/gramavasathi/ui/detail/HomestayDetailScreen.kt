package com.gramavasathi.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramavasathi.ui.components.NetworkImage
import com.gramavasathi.ui.components.tierText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomestayDetailScreen(id: String, onBook: (String) -> Unit, vm: DetailViewModel = viewModel()) {
    val h = vm.homestay.collectAsStateWithLifecycle().value
    LaunchedEffect(id) { vm.load(id) }

    if (h == null) {
        Text("Loading...", modifier = Modifier.padding(16.dp))
        return
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp)) {
        item {
            NetworkImage(h.image_urls.firstOrNull().orEmpty(), Modifier.fillMaxWidth())
            Text(h.name, style = MaterialTheme.typography.headlineMedium)
            Text("📍 ${h.village}, ${h.district}")
            Text("₹${h.price_per_night} / night · Max ${h.max_guests} guests")
            Text("★ ${h.rating} · ${h.review_count} reviews")
            Text(if (h.is_verified) "Verified Host ✓" else "Not Verified")
            Text("Host Score: ${h.host_readiness_score}/100 · ${tierText(h.host_readiness_score)}")
        }
        item {
            Text("What You'll Do 🌾", style = MaterialTheme.typography.titleLarge)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                h.activities.forEach { Text(it, modifier = Modifier.padding(6.dp)) }
            }
        }
        item {
            Text("What's Included 🏡", style = MaterialTheme.typography.titleLarge)
        }
        items(h.amenities.chunked(2)) { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                row.forEach { Text("✓ $it") }
            }
        }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("About the Host", style = MaterialTheme.typography.titleLarge)
                    Text(h.host_name)
                    Text("Warm village host focused on comfort and local culture.")
                }
            }
        }
        item {
            Button(onClick = { onBook(h.id) }, modifier = Modifier.fillMaxWidth()) {
                Text("₹${h.price_per_night}/night · Book Your Stay →")
            }
        }
    }
}
