package com.gramavasathi.data.model

data class ChecklistItem(
    val id: String,
    val category: String,     // "Hygiene" | "Comfort" | "Safety" | "Food" | "Experience"
    val title: String,
    val description: String,
    val points: Int           // weight for score calculation (total = 100)
)
