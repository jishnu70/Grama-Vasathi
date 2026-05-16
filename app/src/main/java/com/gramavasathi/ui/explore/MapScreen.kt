package com.gramavasathi.ui.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(onBack: () -> Unit, onDetails: (String) -> Unit, vm: ExploreViewModel = viewModel()) {
    val context = LocalContext.current
    val homestays = vm.all.collectAsStateWithLifecycle().value
    val selected = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { if (homestays.isEmpty()) vm.load() }

    Column {
        Button(onClick = onBack, modifier = Modifier.padding(8.dp)) { Text("← Back") }
        AndroidView(
            factory = {
                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    controller.setZoom(8.0)
                    controller.setCenter(GeoPoint(14.5204, 75.7224))
                    setMultiTouchControls(true)
                }
            },
            update = { map ->
                map.overlays.removeAll { it is Marker }
                homestays.forEach { h ->
                    val marker = Marker(map)
                    marker.position = GeoPoint(h.latitude, h.longitude)
                    marker.title = h.name
                    marker.snippet = "${h.village} · ₹${h.price_per_night}/night"
                    marker.setOnMarkerClickListener { _, _ -> selected.value = h.id; true }
                    map.overlays.add(marker)
                }
                map.invalidate()
            },
            modifier = Modifier.weight(1f).fillMaxSize()
        )
        selected.value?.let { id ->
            val h = homestays.firstOrNull { it.id == id }
            if (h != null) {
                Card(Modifier.fillMaxWidth().padding(12.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(h.name)
                        Text("₹${h.price_per_night}/night")
                        Button(onClick = { onDetails(h.id) }) { Text("View Details") }
                    }
                }
            }
        }
    }
}
