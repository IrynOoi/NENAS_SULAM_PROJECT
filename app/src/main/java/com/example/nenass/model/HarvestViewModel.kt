//HarvestViewModel.kt
package com.example.nenass.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HarvestViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _crops = MutableStateFlow<List<Crop>>(emptyList())
    val crops = _crops.asStateFlow()

    // Sign in user
    fun signIn(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener {
            fetchCrops()
        }.addOnFailureListener {
            Log.e("HarvestViewModel", "Sign in failed", it)
        }
    }

    fun updateLocalCrop(updated: Crop) {
        val current = _crops.value.toMutableList()
        val index = current.indexOfFirst { it.id == updated.id }

        if (index != -1) {
            current[index] = updated
            _crops.value = current
        }
    }


    // Fetch crops from Firestore
    fun fetchCrops() {
        val uid = auth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            Log.e("HarvestViewModel", "User not logged in")
            return
        }

        db.collection("crops")
            .whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { result ->
                val cropList = result.documents.mapNotNull { doc ->
                    val crop = doc.toObject(Crop::class.java)
                    crop?.id = doc.id // Firestore ID
                    crop
                }
                _crops.value = cropList
            }
            .addOnFailureListener { e ->
                Log.e("HarvestViewModel", "Error fetching crops", e)
            }
    }

    // Add a new crop
    fun addCrop() {
        val uid = auth.currentUser?.uid ?: return
        val newCrop = Crop(userId = uid, name = "New Crop", seasons = mutableListOf(Season()))
        db.collection("crops").add(newCrop).addOnSuccessListener {
            fetchCrops()
        }
    }

    // Update existing crop
    fun saveCrop(crop: Crop) {
        val cropId = crop.id
        if (!cropId.isNullOrEmpty()) {
            db.collection("crops").document(cropId).set(crop)
                .addOnSuccessListener { fetchCrops() }
                .addOnFailureListener { e ->
                    Log.e("HarvestViewModel", "Failed to save crop", e)
                }
        } else {
            Log.e("HarvestViewModel", "Cannot save crop: missing ID")
        }
    }

    // Delete crop
    fun deleteCrop(cropId: String) {
        if (cropId.isEmpty()) {
            Log.e("HarvestViewModel", "Cannot delete crop: empty ID")
            return
        }

        db.collection("crops").document(cropId).delete()
            .addOnSuccessListener { fetchCrops() }
            .addOnFailureListener { e ->
                Log.e("HarvestViewModel", "Failed to delete crop", e)
            }
    }
}
