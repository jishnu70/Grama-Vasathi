package com.gramavasathi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.gramavasathi.data.model.Booking
import kotlinx.coroutines.tasks.await

class BookingRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val bookingsCollection = firestore.collection("bookings")

    suspend fun createBooking(booking: Booking): Boolean {
        return try {
            bookingsCollection.add(booking).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
