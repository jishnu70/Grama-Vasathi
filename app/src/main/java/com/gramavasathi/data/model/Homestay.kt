package com.gramavasathi.data.model

data class Homestay(
    val id: String = "",
    val name: String = "",
    val host_name: String = "",
    val village: String = "",
    val district: String = "",
    val state: String = "Karnataka",
    val latitude: Double = .0,
    val longitude: Double = .0,
    val price_per_night: Int = 0,
    val max_guests: Int = 1,
    val description: String = "",
    val image_urls: List<String> = emptyList(),
    val activities: List<String> = emptyList(),
    val amenities: List<String> = emptyList(),
    val host_readiness_score: Int = 0,
    val checklist_completed: Map<String, Boolean> = emptyMap(),
    val is_verified: Boolean = false,
    val rating: Double = 0.0,
    val review_count: Int = 0
)
