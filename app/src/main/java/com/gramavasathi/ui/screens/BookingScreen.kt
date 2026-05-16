package com.gramavasathi.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gramavasathi.Screen
import com.gramavasathi.data.model.Booking
import com.gramavasathi.ui.viewmodel.BookingViewModel
import com.gramavasathi.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    homestayId: String,
    homestayName: String,
    pricePerNight: Int,
    navController: NavController,
    viewModel: BookingViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var guests by remember { mutableStateOf("1") }
    var checkIn by remember { mutableStateOf("") }
    var checkOut by remember { mutableStateOf("") }
    var requests by remember { mutableStateOf("") }
    
    val bookingStatus by viewModel.bookingStatus.observeAsState()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bookingStatus) {
        if (bookingStatus == true) {
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Booking Request Sent! 🌿", style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp) },
            text = { Text("Host will call you within 24 hours to confirm.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.resetStatus()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                ) {
                    Text("Back to Home")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(CreamWhite)
            .padding(20.dp)
    ) {
        Text(text = "Book Your Stay", style = MaterialTheme.typography.displayLarge, fontSize = 24.sp)
        Text(text = homestayName, color = MutedBrown, modifier = Modifier.padding(top = 4.dp))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 10) phone = it },
            label = { Text("Phone Number") },
            prefix = { Text("+91 ") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = guests,
            onValueChange = { guests = it },
            label = { Text("Number of Guests") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = checkIn,
                onValueChange = { },
                label = { Text("Check-in") },
                modifier = Modifier.weight(1f).clickable {
                    val datePicker = DatePickerDialog(context, { _, y, m, d ->
                        val cal = Calendar.getInstance().apply { set(y, m, d) }
                        checkIn = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                    datePicker.show()
                },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = EarthBrown,
                    disabledBorderColor = DividerWarm,
                    disabledLabelColor = MutedBrown
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = checkOut,
                onValueChange = { },
                label = { Text("Check-out") },
                modifier = Modifier.weight(1f).clickable {
                    val datePicker = DatePickerDialog(context, { _, y, m, d ->
                        val cal = Calendar.getInstance().apply { set(y, m, d) }
                        checkOut = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                    datePicker.show()
                },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = EarthBrown,
                    disabledBorderColor = DividerWarm,
                    disabledLabelColor = MutedBrown
                )
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = requests,
            onValueChange = { requests = it },
            label = { Text("Special Requests (Optional)") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Price Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = WarmBeige),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val nights = calculateNights(checkIn, checkOut)
                val total = nights * pricePerNight
                Text(text = "$nights nights × ₹$pricePerNight = ₹$total", color = MutedBrown)
                Text(text = "Grama-Vasathi Service Fee: ₹0", color = LeafGreen, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                Text(text = "Total: ₹$total", style = MaterialTheme.typography.displayLarge, fontSize = 20.sp, modifier = Modifier.padding(top = 12.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                val booking = Booking(
                    homestay_id = homestayId,
                    homestay_name = homestayName,
                    guest_name = name,
                    guest_phone = phone,
                    check_in = checkIn,
                    check_out = checkOut,
                    guests_count = guests.toIntOrNull() ?: 1,
                    total_price = calculateTotal(checkIn, checkOut, pricePerNight)
                )
                viewModel.submitBooking(booking)
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Confirm Booking Request", style = MaterialTheme.typography.headlineMedium, fontSize = 18.sp, color = Color.White)
        }
    }
}

fun calculateNights(checkIn: String, checkOut: String): Int {
    if (checkIn.isBlank() || checkOut.isBlank()) return 1
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        val d1 = sdf.parse(checkIn)
        val d2 = sdf.parse(checkOut)
        val diff = d2.time - d1.time
        val nights = (diff / (1000 * 60 * 60 * 24)).toInt()
        if (nights < 1) 1 else nights
    } catch (e: Exception) {
        1
    }
}

fun calculateTotal(checkIn: String, checkOut: String, price: Int): Int {
    return calculateNights(checkIn, checkOut) * price
}
