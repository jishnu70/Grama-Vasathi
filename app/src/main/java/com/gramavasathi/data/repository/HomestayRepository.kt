package com.gramavasathi.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.gramavasathi.data.model.Homestay
import kotlinx.coroutines.tasks.await

class HomestayRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val homestaysCollection = firestore.collection("homestays")

    suspend fun getAllHomestays(): List<Homestay> {
        return try {
            homestaysCollection.get().await().toObjects(Homestay::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHomestayById(id: String): Homestay? {
        return try {
            homestaysCollection.document(id).get().await().toObject(Homestay::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
