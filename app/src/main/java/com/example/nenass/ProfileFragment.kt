//ProfileFragment.kt
package com.example.nenass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    // Views from fragment_profile.xml
    private lateinit var etName: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var btnUpdate: MaterialButton
    private lateinit var ivProfile: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Initialize Firebase
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Pointing to "Users" node -> UserID
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)

        // 2. Bind Views
        etName = view.findViewById(R.id.name_txtfield)
        etPhone = view.findViewById(R.id.phone_no)
        etEmail = view.findViewById(R.id.tvEmail)
        etAddress = view.findViewById(R.id.address_txtfield)
        btnUpdate = view.findViewById(R.id.button4) // The "Update" button
        ivProfile = view.findViewById(R.id.ivProfileImage)

        // 3. Load Existing Data
        loadUserProfile()

        // 4. Set Update Button Listener
        btnUpdate.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun loadUserProfile() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return // Check if fragment is attached

                if (snapshot.exists()) {
                    // Get data from database
                    val name = snapshot.child("username").value?.toString() ?: ""
                    val phone = snapshot.child("phone").value?.toString() ?: ""
                    val email = snapshot.child("email").value?.toString() ?: ""
                    val address = snapshot.child("address").value?.toString() ?: ""
                    val profileUrl = snapshot.child("profileUrl").value?.toString() ?: ""

                    // Set data to text fields
                    etName.setText(name)
                    etPhone.setText(phone)
                    etEmail.setText(email)
                    etAddress.setText(address)

                    // Load Image using Glide
                    if (profileUrl.isNotEmpty()) {
                        Glide.with(this@ProfileFragment)
                            .load(profileUrl)
                            .placeholder(R.drawable.user_profile) // Ensure this drawable exists
                            .into(ivProfile)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserProfile() {
        // Get data from input fields
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val address = etAddress.text.toString().trim()

        if (name.isEmpty()) {
            etName.error = "Name is required"
            return
        }

        // Create a map of data to update
        val userUpdates = HashMap<String, Any>()
        userUpdates["username"] = name
        userUpdates["phone"] = phone
        userUpdates["email"] = email
        userUpdates["address"] = address
        // Note: "memer" and "search" fields should ideally be updated here too if used for searching

        // Update in Firebase Realtime Database
        databaseReference.updateChildren(userUpdates)
            .addOnSuccessListener {
                Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Update Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}