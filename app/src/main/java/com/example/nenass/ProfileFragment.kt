//ProfileFragment.kt
package com.example.nenass

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import androidx.activity.result.contract.ActivityResultContracts

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var etName: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etAddress: TextInputEditText
    private lateinit var btnUpdate: MaterialButton
    private lateinit var ivProfile: ImageView
    private lateinit var btnEditProfile: ImageButton

    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            selectedImageUri = result.data!!.data
            selectedImageUri?.let { uploadProfileImage(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)

        etName = view.findViewById(R.id.name_txtfield)
        etPhone = view.findViewById(R.id.phone_no)
        etEmail = view.findViewById(R.id.tvEmail)
        etAddress = view.findViewById(R.id.address_txtfield)
        btnUpdate = view.findViewById(R.id.button4)
        ivProfile = view.findViewById(R.id.ivProfileImage)
        btnEditProfile = view.findViewById(R.id.btnEditProfileImage)

        loadUserProfile()

        btnUpdate.setOnClickListener { updateUserProfile() }

        btnEditProfile.setOnClickListener { pickImageFromGallery() }
    }

    private fun loadUserProfile() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return

                if (snapshot.exists()) {
                    val name = snapshot.child("username").value?.toString() ?: ""
                    val phone = snapshot.child("phone").value?.toString() ?: ""
                    val email = snapshot.child("email").value?.toString() ?: ""
                    val address = snapshot.child("address").value?.toString() ?: ""
                    val profileUrl = snapshot.child("profileUrl").value?.toString() ?: ""

                    etName.setText(name)
                    etPhone.setText(phone)
                    etEmail.setText(email)
                    etAddress.setText(address)

                    if (profileUrl.isNotEmpty()) {
                        Glide.with(this@ProfileFragment)
                            .load(profileUrl)
                            .placeholder(R.drawable.user_profile)
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
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val address = etAddress.text.toString().trim()

        if (name.isEmpty()) {
            etName.error = "Name is required"
            return
        }

        val userUpdates = HashMap<String, Any>()
        userUpdates["username"] = name
        userUpdates["phone"] = phone
        userUpdates["email"] = email
        userUpdates["address"] = address

        databaseReference.updateChildren(userUpdates)
            .addOnSuccessListener {
                Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Update Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private fun uploadProfileImage(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance()
            .getReference("profile_images/${auth.currentUser!!.uid}.jpg")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    databaseReference.child("profileUrl").setValue(downloadUrl.toString())
                    Glide.with(this)
                        .load(downloadUrl)
                        .placeholder(R.drawable.user_profile)
                        .into(ivProfile)
                    Toast.makeText(context, "Profile picture updated", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
