package com.gramavasathi.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Booking
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.BookingRepository
import com.gramavasathi.data.repository.HomestayRepository
import com.gramavasathi.utils.nightsBetween
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class BookingViewModel : ViewModel() {
    private val homestayRepo = HomestayRepository()
    private val bookingRepo = BookingRepository()

    private val _homestay = MutableStateFlow<Homestay?>(null)
    val homestay: StateFlow<Homestay?> = _homestay.asStateFlow()

    val guestName = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val guests = MutableStateFlow(1)
    val checkIn = MutableStateFlow("")
    val checkOut = MutableStateFlow("")
    val requests = MutableStateFlow("")

    fun load(id: String) {
        viewModelScope.launch { _homestay.value = homestayRepo.getHomestayById(id) }
    }

    fun total(): Int {
        val h = _homestay.value ?: return 0
        return (nightsBetween(checkIn.value, checkOut.value).coerceAtLeast(1) * h.price_per_night).toInt()
    }

    fun isValid(): Boolean {
        val hasName = guestName.value.isNotBlank()
        val validPhone = phone.value.matches(Regex("^\\d{10}$"))
        val hasDates = checkIn.value.isNotBlank() && checkOut.value.isNotBlank()
        val validStay = nightsBetween(checkIn.value, checkOut.value) >= 1
        return hasName && validPhone && hasDates && validStay
    }

    fun submit(onResult: (Boolean) -> Unit) {
        val h = _homestay.value ?: return onResult(false)
        viewModelScope.launch {
            val booking = Booking(
                id = UUID.randomUUID().toString(),
                homestay_id = h.id,
                homestay_name = h.name,
                guest_name = guestName.value,
                guest_phone = phone.value,
                check_in = checkIn.value,
                check_out = checkOut.value,
                guests_count = guests.value,
                total_price = total()
            )
            onResult(runCatching { bookingRepo.createBooking(booking) }.isSuccess)
        }
    }
}
