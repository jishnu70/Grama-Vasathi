package com.gramavasathi.ui.booking

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate

@Composable
fun BookingScreen(id: String, onDone: () -> Unit, vm: BookingViewModel = viewModel()) {
    val context = LocalContext.current
    val h = vm.homestay.collectAsStateWithLifecycle().value
    var success by remember { mutableStateOf(false) }
    LaunchedEffect(id) { vm.load(id) }

    val pickDate: (Boolean) -> Unit = { isCheckIn ->
        val now = LocalDate.now()
        DatePickerDialog(context, { _, y, m, d ->
            val date = "%04d-%02d-%02d".format(y, m + 1, d)
            if (isCheckIn) vm.checkIn.value = date else vm.checkOut.value = date
        }, now.year, now.monthValue - 1, now.dayOfMonth).show()
    }

    if (h == null) {
        Text("Loading...", modifier = Modifier.padding(16.dp))
        return
    }

    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Text(h.name, style = MaterialTheme.typography.titleLarge)
            Text(h.village)
        }
        item { OutlinedTextField(vm.guestName.collectAsStateWithLifecycle().value, { vm.guestName.value = it }, label = { Text("Your Name") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(vm.phone.collectAsStateWithLifecycle().value, { vm.phone.value = it.filter { c -> c.isDigit() }.take(10) }, label = { Text("Phone Number") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(vm.guests.collectAsStateWithLifecycle().value.toString(), { vm.guests.value = it.toIntOrNull()?.coerceIn(1, 8) ?: 1 }, label = { Text("Guests (1-8)") }, modifier = Modifier.fillMaxWidth()) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { pickDate(true) }) { Text("Check-in") }
                Text(vm.checkIn.collectAsStateWithLifecycle().value)
                Button(onClick = { pickDate(false) }) { Text("Check-out") }
                Text(vm.checkOut.collectAsStateWithLifecycle().value)
            }
        }
        item { OutlinedTextField(vm.requests.collectAsStateWithLifecycle().value, { vm.requests.value = it }, label = { Text("Special Requests") }, modifier = Modifier.fillMaxWidth()) }
        item {
            Text("${com.gramavasathi.utils.nightsBetween(vm.checkIn.value, vm.checkOut.value).coerceAtLeast(1)} nights × ₹${h.price_per_night} = ₹${vm.total()}")
            Text("Grama-Vasathi Service Fee: ₹0")
            Text("Total: ₹${vm.total()}", style = MaterialTheme.typography.titleLarge)
        }
        item {
            Button(onClick = { if (vm.isValid()) vm.submit { ok -> success = ok } }, modifier = Modifier.fillMaxWidth()) {
                Text("Confirm Booking Request")
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
