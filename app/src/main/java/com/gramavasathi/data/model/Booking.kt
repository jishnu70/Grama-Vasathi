package com.gramavasathi.data.model

import com.google.firebase.Timestamp

data class Booking(
    val id: String = "",
    val homestay_id: String = "",
    val homestay_name: String = "",
    val guest_name: String = "",
    val guest_phone: String = "",
    val check_in: String = "",
    val check_out: String = "",
    val guests_count: Int = 1,
    val total_price: Int = 0,
    val status: String = "pending",
    val submitted_at: Timestamp = Timestamp.now()
)
