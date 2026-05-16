package com.gramavasathi.ui.booking

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gramavasathi.ui.components.AppTopBar
import com.gramavasathi.ui.components.SectionHeader
import com.gramavasathi.ui.theme.CreamWhite
import com.gramavasathi.ui.theme.EarthBrown
import com.gramavasathi.ui.theme.MutedBrown
import com.gramavasathi.ui.theme.Terracotta
import com.gramavasathi.ui.theme.WarmBeige
import java.time.LocalDate

@Composable
fun BookingScreen(id: String, onBack: () -> Unit, onDone: () -> Unit, vm: BookingViewModel = viewModel()) {
    val context = LocalContext.current
    val h = vm.homestay.collectAsStateWithLifecycle().value
    val isSubmitting = vm.isSubmitting.collectAsStateWithLifecycle().value
    var success by remember { mutableStateOf(false) }
    var triedSubmit by remember { mutableStateOf(false) }
    LaunchedEffect(id) { vm.load(id) }

    val pickDate: (Boolean) -> Unit = { isCheckIn ->
        val now = LocalDate.now()
        DatePickerDialog(context, { _, y, m, d ->
            val date = "%04d-%02d-%02d".format(y, m + 1, d)
            if (isCheckIn) vm.checkIn.value = date else vm.checkOut.value = date
        }, now.year, now.monthValue - 1, now.dayOfMonth).show()
    }

    Column(Modifier.fillMaxSize().background(CreamWhite)) {
        AppTopBar(title = "Book Your Stay", onBack = onBack)

        if (h == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = EarthBrown)
            }
            return@Column
        }

        val errors = if (triedSubmit) vm.validationErrors() else emptyList()

        LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                SectionHeader(h.name)
                Text("📍 ${h.village}, ${h.district}", color = MutedBrown)
            }

            item {
                OutlinedTextField(
                    value = vm.guestName.collectAsStateWithLifecycle().value,
                    onValueChange = { vm.guestName.value = it },
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.any { it.contains("name", true) }
                )
            }

            item {
                OutlinedTextField(
                    value = vm.phone.collectAsStateWithLifecycle().value,
                    onValueChange = { vm.phone.value = it.filter { c -> c.isDigit() }.take(10) },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    isError = errors.any { it.contains("Phone", true) }
                )
            }

            item {
                Text("Guests", color = EarthBrown, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(onClick = { vm.guests.value = (vm.guests.value - 1).coerceAtLeast(1) }) {
                        Text("−", style = MaterialTheme.typography.headlineMedium)
                    }
                    Text(vm.guests.collectAsStateWithLifecycle().value.toString(), style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = { vm.guests.value = (vm.guests.value + 1).coerceAtMost(8) }) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                    Text("(1–8)", color = MutedBrown)
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { pickDate(true) }) { Text("Check-in") }
                    Text(vm.checkIn.collectAsStateWithLifecycle().value.ifBlank { "Select date" }, color = EarthBrown)
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { pickDate(false) }) { Text("Check-out") }
                    Text(vm.checkOut.collectAsStateWithLifecycle().value.ifBlank { "Select date" }, color = EarthBrown)
                }
            }

            item {
                OutlinedTextField(
                    value = vm.requests.collectAsStateWithLifecycle().value,
                    onValueChange = { vm.requests.value = it },
                    label = { Text("Special Requests") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (errors.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        errors.forEach { err ->
                            Text("• $err", color = Terracotta, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            item {
                Column(
                    Modifier.fillMaxWidth().background(WarmBeige, MaterialTheme.shapes.large).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Price Summary", fontWeight = FontWeight.Bold, color = EarthBrown)
                    val nights = com.gramavasathi.utils.nightsBetween(vm.checkIn.value, vm.checkOut.value).coerceAtLeast(1)
                    Text("$nights nights × ₹${h.price_per_night} = ₹${vm.total()}")
                    Text("Grama-Vasathi Service Fee: ₹0")
                    Text("Total: ₹${vm.total()}", style = MaterialTheme.typography.titleLarge, color = EarthBrown)
                }
            }

            item {
                Button(
                    onClick = {
                        triedSubmit = true
                        if (vm.isValid() && !isSubmitting) {
                            vm.submit { ok -> success = ok }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta, contentColor = Color.White)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Confirm Booking Request")
                    }
                }
            }
        }
    }

    if (success) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Booking Request Sent! 🌿") },
            text = { Text("${h.host_name} will call you within 24 hours to confirm.") },
            confirmButton = { Button(onClick = onDone) { Text("Back to Home") } }
        )
    }
}
