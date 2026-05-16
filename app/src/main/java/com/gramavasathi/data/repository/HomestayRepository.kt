package com.gramavasathi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.gramavasathi.data.model.Homestay
import kotlinx.coroutines.tasks.await

class HomestayRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getAllHomestays(): List<Homestay> {
        return firestore.collection("homestays").get().await().documents.mapNotNull { it.toObject<Homestay>() }
    }

    suspend fun getHomestayById(id: String): Homestay? {
        return firestore.collection("homestays").document(id).get().await().toObject<Homestay>()
    }
}
