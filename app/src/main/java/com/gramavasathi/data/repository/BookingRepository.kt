package com.gramavasathi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.gramavasathi.data.model.Booking
import kotlinx.coroutines.tasks.await

class BookingRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun createBooking(booking: Booking) {
        firestore.collection("bookings").document(booking.id).set(booking).await()
    }
}
