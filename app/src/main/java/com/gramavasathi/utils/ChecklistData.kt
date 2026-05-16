package com.gramavasathi.utils

import com.gramavasathi.data.model.ChecklistItem

val checklistItems = listOf(
    ChecklistItem("clean_sheets", "Hygiene", "Fresh Clean Sheets", "Freshly washed sheets for every guest", 12),
    ChecklistItem("safe_water", "Safety", "Safe Drinking Water (filtered/boiled)", "Filtered or boiled drinking water available", 15),
    ChecklistItem("western_toilet", "Hygiene", "Western-style Toilet Available", "Clean western toilet for guest comfort", 10),
    ChecklistItem("clean_bathroom", "Hygiene", "Clean Bathroom with Soap & Towels", "Soap and clean towels in bathroom", 8),
    ChecklistItem("mosquito_net", "Safety", "Mosquito Nets on All Beds", "Every bed has mosquito protection", 8),
    ChecklistItem("home_cooked", "Food", "Home-cooked Hygienic Meals", "Safe and hygienic local meals", 12),
    ChecklistItem("food_allergies", "Food", "Ask Guests About Food Allergies", "Confirm and track food allergies", 5),
    ChecklistItem("welcome_note", "Experience", "Written Welcome Note in Room", "Simple welcome note for guests", 5),
    ChecklistItem("local_guide", "Experience", "Printed Local Area Guide", "Printed local guide for places and customs", 5),
    ChecklistItem("activity_schedule", "Experience", "Activity Schedule Provided", "Clear schedule for farm activities", 10),
    ChecklistItem("first_aid", "Safety", "First Aid Kit Available", "Basic first aid kit available", 5),
    ChecklistItem("phone_charged", "Comfort", "Phone Charging Point in Room", "Reliable charging point in room", 5)
)

fun scoreTier(score: Int): Pair<String, String> = when (score) {
    in 0..39 -> "🌱 Getting Started" to "red"
    in 40..69 -> "🌿 Almost Ready" to "orange"
    in 70..89 -> "🌾 Guest Ready" to "gold"
    else -> "⭐ Certified Host" to "green"
}
