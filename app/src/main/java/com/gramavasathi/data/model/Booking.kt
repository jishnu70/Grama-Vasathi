package com.gramavasathi.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Booking(
    @DocumentId val id: String = "",
    val homestay_id: String = "",
    val homestay_name: String = "",
    val guest_name: String = "",
    val guest_phone: String = "",
    val check_in: String = "",
    val check_out: String = "",
    val guests_count: Int = 0,
    val total_price: Int = 0,
    val status: String = "pending", // pending | confirmed | cancelled
    val submitted_at: Timestamp = Timestamp.now()
)
