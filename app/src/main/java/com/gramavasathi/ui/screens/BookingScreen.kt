package com.gramavasathi.ui.screens

import android.app.DatePickerDialog
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LeafGreen, modifier = Modifier.size(48.dp)) },
            title = { Text("Booking Confirmed! 🌿", style = Typography.displayLarge, fontSize = 22.sp) },
            text = { Text("Your rural escape is waiting. The host will contact you shortly to finalize the details.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.resetStatus()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EarthBrown),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Explore More")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Confirm Booking", style = Typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = EarthBrown)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CreamWhite)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(CreamWhite)
                .padding(padding)
                .padding(20.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WarmBeige),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).background(EarthBrown, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                    }
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = homestayName, style = Typography.titleLarge, fontSize = 16.sp)
                        Text(text = "Reserved at ₹$pricePerNight / night", style = Typography.bodyMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Traveler Details", style = Typography.titleLarge)
            
            Spacer(modifier = Modifier.height(16.dp))
            PremiumTextField(value = name, onValueChange = { name = it }, label = "Full Name", icon = Icons.Default.Person)
            Spacer(modifier = Modifier.height(16.dp))
            PremiumTextField(value = phone, onValueChange = { if (it.length <= 10) phone = it }, label = "Phone Number", icon = Icons.Default.Phone, prefix = "+91 ")
            Spacer(modifier = Modifier.height(16.dp))
            PremiumTextField(value = guests, onValueChange = { guests = it }, label = "Number of Guests", icon = Icons.Default.Groups)
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Stay Dates", style = Typography.titleLarge)
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    PremiumDateField(value = checkIn, label = "Check-in") {
                        val dp = DatePickerDialog(context, { _, y, m, d ->
                            val c = Calendar.getInstance().apply { set(y, m, d) }
                            checkIn = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.time)
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        dp.show()
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    PremiumDateField(value = checkOut, label = "Check-out") {
                        val dp = DatePickerDialog(context, { _, y, m, d ->
                            val c = Calendar.getInstance().apply { set(y, m, d) }
                            checkOut = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.time)
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        dp.show()
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Additional Info", style = Typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = requests,
                onValueChange = { requests = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Any specific needs or food allergies?") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EarthBrown,
                    unfocusedBorderColor = DividerWarm,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(40.dp))
            
            // Total Price Calculation
            val nights = calculateNights(checkIn, checkOut)
            val total = nights * pricePerNight
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = WarmBeige,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Booking Summary", style = Typography.labelMedium, color = MutedBrown)
                        Text(text = "$nights Nights", style = Typography.labelMedium, color = EarthBrown)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Total Amount", style = Typography.titleLarge, fontSize = 20.sp)
                        Text(text = "₹$total", style = Typography.displayLarge, fontSize = 24.sp, color = Terracotta)
                    }
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
                        total_price = total
                    )
                    viewModel.submitBooking(booking)
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EarthBrown),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(8.dp)
            ) {
                Text("Confirm Booking", style = Typography.titleLarge, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun PremiumTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, prefix: String = "") {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = MutedBrown) },
        prefix = if (prefix.isNotEmpty()) { { Text(prefix) } } else null,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = EarthBrown,
            unfocusedBorderColor = DividerWarm,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

private fun calculateNights(checkIn: String, checkOut: String): Int {
    if (checkIn.isEmpty() || checkOut.isEmpty()) return 1
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date1 = sdf.parse(checkIn)
        val date2 = sdf.parse(checkOut)
        val diff = date2!!.time - date1!!.time
        val nights = (diff / (1000 * 60 * 60 * 24)).toInt()
        if (nights < 1) 1 else nights
    } catch (e: Exception) {
        1
    }
}

@Composable
fun PremiumDateField(value: String, label: String, onClick: () -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { },
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = MutedBrown) },
        enabled = false,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = EarthBrown,
            disabledBorderColor = DividerWarm,
            disabledLabelColor = MutedBrown,
            disabledContainerColor = Color.White
        )
    )
}
