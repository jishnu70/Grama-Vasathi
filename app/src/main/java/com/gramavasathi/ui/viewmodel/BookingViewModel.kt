package com.gramavasathi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Booking
import com.gramavasathi.data.repository.BookingRepository
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {
    private val repository = BookingRepository()
    private val _bookingStatus = MutableLiveData<Boolean?>(null)
    val bookingStatus: LiveData<Boolean?> = _bookingStatus

    fun submitBooking(booking: Booking) {
        viewModelScope.launch {
            _bookingStatus.value = repository.createBooking(booking)
        }
    }

    fun resetStatus() {
        _bookingStatus.value = null
    }
}
