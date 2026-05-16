package com.gramavasathi.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.gramavasathi.data.model.Homestay
import kotlinx.coroutines.tasks.await

object FirebaseSeeder {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun seedAll() {
        val homestays = listOf(
            Homestay(
                id = "h1",
                name = "Nandi Hills Organic Farm",
                host_name = "Raju & Meena Gowda",
                village = "Devanahalli",
                district = "Bengaluru Rural",
                latitude = 13.2468, longitude = 77.7139,
                price_per_night = 850,
                max_guests = 4,
                description = "Wake up to mist-covered hills and the smell of fresh filter coffee. Our 3-acre organic farm grows ragi, tomatoes, and marigolds. Guests join morning milking and evening campfire.",
                image_urls = listOf(
                    "https://images.unsplash.com/photo-1500382017468-9049fed747ef?q=80&w=1000",
                    "https://images.unsplash.com/photo-1590001158193-79cd7c703f89?q=80&w=1000"
                ),
                activities = listOf("Cow Milking", "Field Plowing", "Bird Watching", "Local Cooking"),
                amenities = listOf("Clean Sheets", "Safe Water", "Western Toilet", "Hot Water", "Home-cooked Meals"),
                host_readiness_score = 87,
                checklist_completed = mapOf("clean_sheets" to true, "safe_water" to true, "western_toilet" to true, "clean_bathroom" to true, "mosquito_net" to true, "home_cooked" to true, "food_allergies" to true, "welcome_note" to true, "local_guide" to false, "activity_schedule" to true, "first_aid" to true, "phone_charged" to false),
                is_verified = true, rating = 4.7, review_count = 23
            ),
            Homestay(
                id = "h2",
                name = "Malnad Spice Garden Retreat",
                host_name = "Suresh & Kavitha Hegde",
                village = "Sringeri",
                district = "Chikkamagaluru",
                latitude = 13.4136, longitude = 75.2553,
                price_per_night = 1200,
                max_guests = 6,
                description = "Nestled in the Western Ghats, our cardamom and pepper estate offers the real Malnad experience. Monsoon walks through spice gardens, waterfall visits, and authentic Malnad cuisine.",
                image_urls = listOf(
                    "https://images.unsplash.com/photo-1523348837708-15d4a09cfac2?q=80&w=1000",
                    "https://images.unsplash.com/photo-1444858291040-58f756a3bdd6?q=80&w=1000"
                ),
                activities = listOf("Herb Garden", "Nature Walk", "Bird Watching", "Local Cooking", "Sunrise Trek"),
                amenities = listOf("Clean Sheets", "Safe Water", "Western Toilet", "Hot Water", "Home-cooked Meals", "First Aid"),
                host_readiness_score = 95,
                checklist_completed = mapOf("clean_sheets" to true, "safe_water" to true, "western_toilet" to true, "clean_bathroom" to true, "mosquito_net" to true, "home_cooked" to true, "food_allergies" to true, "welcome_note" to true, "local_guide" to true, "activity_schedule" to true, "first_aid" to true, "phone_charged" to true),
                is_verified = true, rating = 4.9, review_count = 41
            ),
            Homestay(
                id = "h3",
                name = "Tungabhadra Riverside Home",
                host_name = "Basavaraj & Asha Nayak",
                village = "Hospet",
                district = "Vijayanagara",
                latitude = 15.2689, longitude = 76.3909,
                price_per_night = 650,
                max_guests = 3,
                description = "A humble home beside the Tungabhadra river. Row a coracle at dawn, visit Hampi ruins nearby, and taste jowar rotti with groundnut chutney straight from Asha's kitchen.",
                image_urls = listOf(
                    "https://images.unsplash.com/photo-1473496169904-658ba7c44d8a?q=80&w=1000",
                    "https://images.unsplash.com/photo-1501785888041-af3ef285b470?q=80&w=1000"
                ),
                activities = listOf("Fishing", "Nature Walk", "Local Cooking", "Bird Watching"),
                amenities = listOf("Clean Sheets", "Safe Water", "Home-cooked Meals"),
                host_readiness_score = 58,
                checklist_completed = mapOf("clean_sheets" to true, "safe_water" to true, "western_toilet" to false, "clean_bathroom" to true, "mosquito_net" to true, "home_cooked" to true, "food_allergies" to false, "welcome_note" to false, "local_guide" to false, "activity_schedule" to false, "first_aid" to false, "phone_charged" to false),
                is_verified = false, rating = 4.1, review_count = 8
            ),
            Homestay(
                id = "h4",
                name = "Coorg Coffee Estate Bungalow",
                host_name = "Dev & Priya Thimmaiah",
                village = "Madikeri",
                district = "Kodagu",
                latitude = 12.4244, longitude = 75.7382,
                price_per_night = 1800,
                max_guests = 5,
                description = "An heirloom coffee estate bungalow with colonial-era architecture. Help with coffee plucking in season, learn Coorgi pork curry, and trek to Abbey Falls.",
                image_urls = listOf(
                    "https://images.unsplash.com/photo-1500673922987-e212871fec22?q=80&w=1000",
                    "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?q=80&w=1000"
                ),
                activities = listOf("Sunrise Trek", "Bird Watching", "Local Cooking", "Herb Garden", "Nature Walk"),
                amenities = listOf("Clean Sheets", "Safe Water", "Western Toilet", "Hot Water", "Home-cooked Meals", "First Aid"),
                host_readiness_score = 100,
                checklist_completed = mapOf("clean_sheets" to true, "safe_water" to true, "western_toilet" to true, "clean_bathroom" to true, "mosquito_net" to true, "home_cooked" to true, "food_allergies" to true, "welcome_note" to true, "local_guide" to true, "activity_schedule" to true, "first_aid" to true, "phone_charged" to true),
                is_verified = true, rating = 5.0, review_count = 67
            ),
            Homestay(
                id = "h5",
                name = "Deccan Plateau Dry Farm",
                host_name = "Siddappa & Yallamma Patil",
                village = "Ron",
                district = "Gadag",
                latitude = 15.7025, longitude = 75.7129,
                price_per_night = 500,
                max_guests = 4,
                description = "Experience north Karnataka's stark, beautiful dry-land farming. Bullocks still plow these fields. Sleep under stars with no light pollution, eat bajra rotti and enne-gayi.",
                image_urls = listOf(
                    "https://images.unsplash.com/photo-1500382017468-9049fed747ef?q=80&w=1000",
                    "https://images.unsplash.com/photo-1590001158193-79cd7c703f89?q=80&w=1000"
                ),
                activities = listOf("Field Plowing", "Local Cooking", "Sunrise Trek"),
                amenities = listOf("Clean Sheets", "Safe Water", "Home-cooked Meals"),
                host_readiness_score = 42,
                checklist_completed = mapOf("clean_sheets" to true, "safe_water" to true, "western_toilet" to false, "clean_bathroom" to false, "mosquito_net" to true, "home_cooked" to true, "food_allergies" to false, "welcome_note" to false, "local_guide" to false, "activity_schedule" to false, "first_aid" to false, "phone_charged" to false),
                is_verified = false, rating = 3.9, review_count = 5
            )
        )

        val batch = firestore.batch()
        homestays.forEach { homestay ->
            val docRef = firestore.collection("homestays").document(homestay.id)
            batch.set(docRef, homestay)
        }
        batch.commit().await()
    }
}
