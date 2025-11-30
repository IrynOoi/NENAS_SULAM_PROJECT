//HarvestViewModel.kt
package com.example.nenass.model

import androidx.lifecycle.ViewModel
import com.example.nenass.model.Crop
import com.example.nenass.model.Season
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HarvestViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    // Replaces this.state.crops
    private val _crops = MutableStateFlow<List<Crop>>(emptyList())
    val crops = _crops.asStateFlow()

    // Replaces signInWithEmail logic
    fun signIn(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
            fetchCrops()
        }
    }

    // Replaces fetchCrops logic
    fun fetchCrops() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("crops")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { result ->
                // Ensure Crop class has a no-argument constructor (Data classes with default values work automatically)
                val cropList = result.toObjects(Crop::class.java)
                _crops.value = cropList
            }
    }

    // Replaces addCrop logic
    fun addCrop() {
        val uid = auth.currentUser?.uid ?: return
        // Note: Ensure Crop.kt has the 'userId' field added
        val newCrop = Crop(userId = uid, name = "", seasons = mutableListOf(Season()))

        db.collection("crops").add(newCrop).addOnSuccessListener { documentReference ->
            // Optional: Update the local object with the new ID if needed immediately
            // or just re-fetch
            fetchCrops()
        }
    }

    // Replaces updateCrop and saveAllCrops logic
    fun saveCrop(crop: Crop) {
        val cropId = crop.id
        if (cropId != null && cropId.isNotEmpty()) {
            db.collection("crops").document(cropId).set(crop)
                .addOnSuccessListener { fetchCrops() }
        }
    }

    fun deleteCrop(cropId: String) {
        db.collection("crops").document(cropId).delete()
            .addOnSuccessListener { fetchCrops() }
    }
}