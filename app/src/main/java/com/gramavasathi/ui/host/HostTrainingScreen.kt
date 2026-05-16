package com.gramavasathi.ui.host

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramavasathi.ui.components.tierText
import com.gramavasathi.ui.components.tierColor

@Composable
fun HostTrainingScreen(vm: HostViewModel = viewModel()) {
    val step by vm.step.collectAsStateWithLifecycle()
    val checked by vm.checked.collectAsStateWithLifecycle()
    val score = vm.score()
    val ctx = LocalContext.current

    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Text("Step: ${vm.categories[step]}", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                vm.categories.forEachIndexed { i, _ ->
                    Text(if (i == step) "●" else if (i < step) "◉" else "○", color = if (i == step) Color(0xFFE8A830) else Color(0xFF6B3D14))
                }
            }
        }

        if (vm.categories[step] != "Score") {
            items(vm.currentItems()) { item ->
                Row(Modifier.fillMaxWidth()) {
                    Checkbox(checked = checked[item.id] == true, onCheckedChange = { vm.toggle(item.id) })
                    Column {
                        Text(item.title)
                        Text(item.description)
                        Text("+${item.points} pts")
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = vm::back, enabled = step > 0) { Text("← Back") }
                    Button(onClick = vm::next) { Text(if (step < 4) "Next Step →" else "See My Full Score →") }
                }
            }
        } else {
            item {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(score.toString(), style = MaterialTheme.typography.displayLarge)
                    Text(tierText(score), color = tierColor(score))
                    vm.categoryBreakdown().forEach { (cat, v) -> Text("$cat: ${v.first}/${v.second}") }
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "I scored $score/100 on Grama-Vasathi's Host Readiness Check! 🌾")
                        }
                        ctx.startActivity(Intent.createChooser(intent, "Share Score"))
                    }) { Text("Share My Score") }
                    Button(onClick = { android.widget.Toast.makeText(ctx, "Coming Soon! We'll contact you.", android.widget.Toast.LENGTH_SHORT).show() }) { Text("List My Home-stay") }
                }
            }
        }

        item {
            Column(Modifier.fillMaxWidth().background(Color(0xFFF5E6C8)).padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Your Score So Far: $score / 100")
                LinearProgressIndicator(progress = { score / 100f }, modifier = Modifier.fillMaxWidth(), color = tierColor(score))
                Text(tierText(score), color = tierColor(score))
            }
        }
    }
}
