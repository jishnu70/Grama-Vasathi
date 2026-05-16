package com.gramavasathi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.gramavasathi.data.model.Homestay
import kotlinx.coroutines.tasks.await

class HomestayRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val homestaysCollection = firestore.collection("homestays")

    private val hardcodedHomestays = listOf(
        Homestay(
            id = "hc1",
            name = "Gokula Farm Stay",
            host_name = "Manjunath Hegde",
            village = "Sirsi",
            district = "Uttara Kannada",
            price_per_night = 1200,
            max_guests = 4,
            description = "Experience the authentic life of a dairy farmer. Help us with cow milking and enjoy fresh organic meals.",
            image_urls = listOf("https://images.unsplash.com/photo-1500382017468-9049fee74a62?auto=format&fit=crop&w=800&q=80", "https://images.unsplash.com/photo-1500076656116-558758c991c1?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🐄 ಹಾಲು ಕರೆಯುವುದು", "🍳 ಅಡುಗೆ ತಯಾರಿ", "🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ"),
            amenities = listOf("Traditional Bath", "Organic Food", "Parking"),
            host_readiness_score = 95,
            is_verified = true,
            rating = 4.8,
            review_count = 24
        ),
        Homestay(
            id = "hc2",
            name = "Vanya River Retreat",
            host_name = "Ravi Kumar",
            village = "Mundkur",
            district = "Udupi",
            price_per_night = 2500,
            max_guests = 6,
            description = "A peaceful retreat by the Shambhavi river. Perfect for bird watching and traditional fishing.",
            image_urls = listOf("https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=800&q=80", "https://images.unsplash.com/photo-1470770841072-f978cf4d019e?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🎣 ಮೀನುಗಾರಿಕೆ", "🐦 ಪಕ್ಷಿ ವೀಕ್ಷಣೆ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("River View", "Hot Water", "Local Guide"),
            host_readiness_score = 88,
            is_verified = true,
            rating = 4.5,
            review_count = 15
        ),
        Homestay(
            id = "hc3",
            name = "Malnad Mist Homestay",
            host_name = "Savitha Devi",
            village = "Kalasa",
            district = "Chikkamagaluru",
            price_per_night = 1800,
            max_guests = 5,
            description = "Nestled in the coffee estates, this stay offers amazing sunrise treks and field plowing experiences.",
            image_urls = listOf("https://images.unsplash.com/photo-1444858291040-58f756a3bdd6?auto=format&fit=crop&w=800&q=80", "https://images.unsplash.com/photo-1418489098061-ce87b5dc3aee?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🌅 ಸೂರ್ಯೋದಯ ಚಾರಣ", "🌾 ಹೊಲ ಉಳುವುದು", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("Bonfire", "Trekking Gear", "Estate Tour"),
            host_readiness_score = 92,
            is_verified = true,
            rating = 4.9,
            review_count = 32
        ),
        Homestay(
            id = "hc4",
            name = "Coorg Heritage Home",
            host_name = "Appachu",
            village = "Madikeri",
            district = "Kodagu",
            price_per_night = 3500,
            max_guests = 8,
            description = "A 100-year-old heritage home in the heart of Coorg. Experience local Kodava culture and cuisine.",
            image_urls = listOf("https://images.unsplash.com/photo-1542718610-a1d656d1884c?auto=format&fit=crop&w=800&q=80", "https://images.unsplash.com/photo-1518780664697-55e3ad937233?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🍳 ಅಡುಗೆ ತಯಾರಿ", "🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ", "🌅 ಸೂರ್ಯೋದಯ ಚಾರಣ"),
            amenities = listOf("Heritage Architecture", "Wi-Fi", "Library"),
            host_readiness_score = 98,
            is_verified = true,
            rating = 4.7,
            review_count = 45
        ),
        Homestay(
            id = "hc5",
            name = "Coastal Coconut Grove",
            host_name = "Sathish Nayak",
            village = "Kumta",
            district = "Uttara Kannada",
            price_per_night = 1500,
            max_guests = 4,
            description = "Stay amidst a lush coconut grove near the beach. Learn how to climb coconut trees and enjoy fresh toddy.",
            image_urls = listOf("https://images.unsplash.com/photo-1505881502353-a1986add3732?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🎣 ಮೀನುಗಾರಿಕೆ", "🍳 ಅಡುಗೆ ತಯಾರಿ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("Hammocks", "Beach Access", "Open Air Kitchen"),
            host_readiness_score = 85,
            is_verified = true,
            rating = 4.6,
            review_count = 18
        ),
        Homestay(
            id = "hc6",
            name = "Areca Nut Heaven",
            host_name = "Ganesh Bhat",
            village = "Thirthahalli",
            district = "Shimoga",
            price_per_night = 2000,
            max_guests = 6,
            description = "A traditional house surrounded by areca nut plantations and pepper vines.",
            image_urls = listOf("https://images.unsplash.com/photo-1516253593875-bd7ba052fbc5?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🌾 ಹೊಲ ಉಳುವುದು", "🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ", "🐦 ಪಕ್ಷಿ ವೀಕ್ಷಣೆ"),
            amenities = listOf("Traditional Bath", "Home Library", "Veranda"),
            host_readiness_score = 90,
            is_verified = true,
            rating = 4.7,
            review_count = 21
        ),
        Homestay(
            id = "hc7",
            name = "Sahyadri Siri Farm",
            host_name = "Lakshmi Devi",
            village = "Siddapura",
            district = "Kodagu",
            price_per_night = 2800,
            max_guests = 5,
            description = "High-altitude coffee and cardamom farm with a focus on sustainable agriculture in the heart of Sahyadri.",
            image_urls = listOf("https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🌅 ಸೂರ್ಯೋದಯ ಚಾರಣ", "🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ", "🍳 ಅಡುಗೆ ತಯಾರಿ"),
            amenities = listOf("Wi-Fi", "Solar Power", "Organic Meals"),
            host_readiness_score = 94,
            is_verified = true,
            rating = 4.9,
            review_count = 29
        ),
        Homestay(
            id = "hc8",
            name = "Malgudi Heritage",
            host_name = "Swami Nathan",
            village = "Agumbe",
            district = "Shimoga",
            price_per_night = 1600,
            max_guests = 4,
            description = "Stay in the village where Malgudi Days was filmed. Experience the rainiest place in South India.",
            image_urls = listOf("https://images.unsplash.com/photo-1473448912268-2022ce9509d8?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🐦 ಪಕ್ಷಿ ವೀಕ್ಷಣೆ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ", "🌅 ಸೂರ್ಯೋದಯ ಚಾರಣ"),
            amenities = listOf("Local Guide", "Rain Gear", "Vintage Rooms"),
            host_readiness_score = 89,
            is_verified = true,
            rating = 4.4,
            review_count = 37
        ),
        Homestay(
            id = "hc9",
            name = "Spice Garden Retreat",
            host_name = "Naseer Khan",
            village = "Kattemane",
            district = "Uttara Kannada",
            price_per_night = 2200,
            max_guests = 6,
            description = "Learn about vanilla, pepper, and cinnamon cultivation in our multi-crop spice garden.",
            image_urls = listOf("https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ", "🍳 ಅಡುಗೆ ತಯಾರಿ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("Hot Water", "Parking", "Estate Walk"),
            host_readiness_score = 91,
            is_verified = true,
            rating = 4.6,
            review_count = 12
        ),
        Homestay(
            id = "hc10",
            name = "Silk Route Stay",
            host_name = "Ramesh Babu",
            village = "Sidlaghatta",
            district = "Chikkaballapur",
            price_per_night = 1100,
            max_guests = 3,
            description = "Witness the journey of silk from cocoon to fabric in the heart of Karnataka's silk belt.",
            image_urls = listOf("https://images.unsplash.com/photo-1508817628294-5a453fa0b8fb?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🐄 ಹಾಲು ಕರೆಯುವುದು", "🍳 ಅಡುಗೆ ತಯಾರಿ", "🌾 ಹೊಲ ಉಳುವುದು"),
            amenities = listOf("Local Food", "Village Tour", "Simple Rooms"),
            host_readiness_score = 82,
            is_verified = false,
            rating = 4.2,
            review_count = 8
        ),
        Homestay(
            id = "hc11",
            name = "Cauvery Bank Homestay",
            host_name = "Poovanna",
            village = "Kushal Nagar",
            district = "Kodagu",
            price_per_night = 3000,
            max_guests = 10,
            description = "Spacious homestay on the banks of River Cauvery. Great for large families.",
            image_urls = listOf("https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🎣 ಮೀನುಗಾರಿಕೆ", "🐦 ಪಕ್ಷಿ ವೀಕ್ಷಣೆ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("River Access", "Large Veranda", "Bonfire"),
            host_readiness_score = 96,
            is_verified = true,
            rating = 4.8,
            review_count = 56
        ),
        Homestay(
            id = "hc12",
            name = "Granite Hill Farm",
            host_name = "Venkatesh",
            village = "Ramanagara",
            district = "Ramanagara",
            price_per_night = 1900,
            max_guests = 5,
            description = "Stay amidst the famous silk-farming and rock-climbing landscapes of Ramanagara.",
            image_urls = listOf("https://images.unsplash.com/photo-1470770841072-f978cf4d019e?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🌅 ಸೂರ್ಯೋದಯ ಚಾರಣ", "🌾 ಹೊಲ ಉಳುವುದು", "🐄 ಹಾಲು ಕರೆಯುವುದು"),
            amenities = listOf("Trekking Help", "Local Food", "Rock Views"),
            host_readiness_score = 87,
            is_verified = true,
            rating = 4.5,
            review_count = 14
        ),
        Homestay(
            id = "hc13",
            name = "Mango Orchard Residency",
            host_name = "Suresh Gowda",
            village = "Channapatna",
            district = "Mandya",
            price_per_night = 1400,
            max_guests = 4,
            description = "Experience life in a mango orchard. Best visited during the summer harvest season.",
            image_urls = listOf("https://images.unsplash.com/photo-1595124253848-21fe24ca4f01?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🍳 ಅಡುಗೆ ತಯಾರಿ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ", "🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ"),
            amenities = listOf("Mango Picking", "Parking", "Traditional Meals"),
            host_readiness_score = 84,
            is_verified = true,
            rating = 4.3,
            review_count = 11
        ),
        Homestay(
            id = "hc14",
            name = "Western Ghats Hideout",
            host_name = "Anjali Murthy",
            village = "Sakleshpur",
            district = "Hassan",
            price_per_night = 2600,
            max_guests = 6,
            description = "Secluded cottage in the hills of Sakleshpur. Perfect for writers and nature lovers.",
            image_urls = listOf("https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🐦 ಪಕ್ಷಿ ವೀಕ್ಷಣೆ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ", "🌅 ಸೂರ್ಯೋದಯ ಚಾರಣ"),
            amenities = listOf("Library", "Hot Water", "Private Balcony"),
            host_readiness_score = 93,
            is_verified = true,
            rating = 4.9,
            review_count = 22
        ),
        Homestay(
            id = "hc15",
            name = "Dandeli Jungle Stay",
            host_name = "Irfan Ahmed",
            village = "Joida",
            district = "Uttara Kannada",
            price_per_night = 3200,
            max_guests = 8,
            description = "Live inside the Kali Tiger Reserve buffer zone. High chances of spotting wildlife.",
            image_urls = listOf("https://images.unsplash.com/photo-1542103749-8ef59b94f47e?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🐦 ಪಕ್ಷಿ ವೀಕ್ಷಣೆ", "🎣 ಮೀನುಗಾರಿಕೆ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("Safari Help", "Forest Guide", "Simple Meals"),
            host_readiness_score = 90,
            is_verified = true,
            rating = 4.7,
            review_count = 41
        ),
        Homestay(
            id = "hc16",
            name = "Traditional Tile House",
            host_name = "Baskara Rao",
            village = "Moodabidri",
            district = "Dakshina Kannada",
            price_per_night = 1300,
            max_guests = 4,
            description = "Stay in a house with traditional Mangalorean red tiles and wooden carvings.",
            image_urls = listOf("https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🍳 ಅಡುಗೆ ತಯಾರಿ", "🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ", "🎣 ಮೀನುಗಾರಿಕೆ"),
            amenities = listOf("Temple Tour", "Coastal Food", "Cool Interiors"),
            host_readiness_score = 88,
            is_verified = true,
            rating = 4.6,
            review_count = 19
        ),
        Homestay(
            id = "hc17",
            name = "Pineapple Patch",
            host_name = "Nagesh",
            village = "Manipal",
            district = "Udupi",
            price_per_night = 1700,
            max_guests = 5,
            description = "Located near the university town, this farm specializes in organic pineapple farming.",
            image_urls = listOf("https://images.unsplash.com/photo-1550592704-6c76defa9985?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🌾 ಹೊಲ ಉಳುವುದು", "🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("Wi-Fi", "Organic Store", "Cycle Rental"),
            host_readiness_score = 86,
            is_verified = true,
            rating = 4.4,
            review_count = 25
        ),
        Homestay(
            id = "hc18",
            name = "Heritage Pottery Farm",
            host_name = "Kumbara",
            village = "Khanapur",
            district = "Belgaum",
            price_per_night = 1550,
            max_guests = 4,
            description = "Stay with a family of traditional potters. Try your hand at the potter's wheel.",
            image_urls = listOf("https://images.unsplash.com/photo-1511497584788-876760111969?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🍳 ಅಡುಗೆ ತಯಾರಿ", "🌾 ಹೊಲ ಉಳುವುದು", "🌿 ಗಿಡಮೂಲಿಕೆ ತೋಟ"),
            amenities = listOf("Pottery Class", "Simple Living", "Village Meals"),
            host_readiness_score = 83,
            is_verified = true,
            rating = 4.5,
            review_count = 13
        ),
        Homestay(
            id = "hc19",
            name = "Mist Valley Estate",
            host_name = "Priya Shetty",
            village = "Baba Budangiri",
            district = "Chikkamagaluru",
            price_per_night = 4000,
            max_guests = 12,
            description = "Large luxury estate house overlooking the valley. Great for corporate retreats or weddings.",
            image_urls = listOf("https://images.unsplash.com/photo-1444858291040-58f756a3bdd6?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🌅 ಸೂರ್ಯೋದಯ ಚಾರಣ", "🐦 ಪಕ್ಷಿ ವೀಕ್ಷಣೆ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("Conference Room", "Full Staff", "Bonfire"),
            host_readiness_score = 97,
            is_verified = true,
            rating = 4.9,
            review_count = 34
        ),
        Homestay(
            id = "hc20",
            name = "Sunflower Plains",
            host_name = "Mallaiah",
            village = "Gundlupet",
            district = "Chamarajanagar",
            price_per_night = 1000,
            max_guests = 2,
            description = "Small eco-hut in the middle of seasonal sunflower fields near Bandipur.",
            image_urls = listOf("https://images.unsplash.com/photo-1596431281204-7489873d61a2?auto=format&fit=crop&w=800&q=80"),
            activities = listOf("🐦 ಪಕ್ಷಿ ವೀಕ್ಷಣೆ", "🌅 ಸೂರ್ಯೋದಯ ಚಾರಣ", "🏞️ ಪ್ರಕೃತಿ ನಡಿಗೆ"),
            amenities = listOf("Eco Friendly", "Stargazing", "Outdoor Shower"),
            host_readiness_score = 81,
            is_verified = false,
            rating = 4.1,
            review_count = 7
        )
    )

    suspend fun getAllHomestays(): List<Homestay> {
        return try {
            val remote = homestaysCollection.get().await().toObjects(Homestay::class.java)
            hardcodedHomestays + remote
        } catch (e: Exception) {
            hardcodedHomestays
        }
    }

    suspend fun getHomestayById(id: String): Homestay? {
        val hardcoded = hardcodedHomestays.find { it.id == id }
        if (hardcoded != null) return hardcoded

        return try {
            homestaysCollection.document(id).get().await().toObject(Homestay::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
