//ShowAdapter.kt
package com.example.nenass.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nenass.OthersProfile
import com.example.nenass.R
import com.example.nenass.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class ShowAdapter(
    private val context: Context,
    private val dataList: List<UserModel>
) : RecyclerView.Adapter<ShowAdapter.ViewHolder>() {

    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile: ImageView = itemView.findViewById(R.id.profile_image)
        val username: TextView = itemView.findViewById(R.id.textView4)
        val memer: TextView = itemView.findViewById(R.id.Memer)
        val btnFollow: Button = itemView.findViewById(R.id.button2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.connection_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]

        // Set user info
        holder.username.text = user.username ?: "Unknown"
        holder.memer.text = user.memer ?: ""
        user.profileUrl?.let {
            Picasso.get().load(it).placeholder(R.drawable.user_profile).into(holder.profile)
        }

        // Hide button if this is current user
        if (currentUser?.uid == user.user_id) {
            holder.btnFollow.visibility = View.GONE
        } else {
            holder.btnFollow.visibility = View.VISIBLE

            // Observe follow status in real-time
            observeFollowStatus(user.user_id ?: return, holder.btnFollow)
        }

        // Click follow/unfollow
        holder.btnFollow.setOnClickListener {
            val targetId = user.user_id ?: return@setOnClickListener
            val currentId = currentUser?.uid ?: return@setOnClickListener
            val followRef = FirebaseDatabase.getInstance().getReference("Follow")

            if (holder.btnFollow.text == "Follow") {
                // Follow immediately
                holder.btnFollow.text = "Following"

                followRef.child(currentId).child("following").child(targetId).setValue(true)
                followRef.child(targetId).child("followers").child(currentId).setValue(true)
            } else {
                // Unfollow immediately
                holder.btnFollow.text = "Follow"

                followRef.child(currentId).child("following").child(targetId).removeValue()
                followRef.child(targetId).child("followers").child(currentId).removeValue()
            }
        }

        // Click profile image to open other profile
        holder.profile.setOnClickListener {
            val uid = user.user_id ?: return@setOnClickListener
            val intent = Intent(context, OthersProfile::class.java)
            intent.putExtra("uid", uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dataList.size

    // Real-time follow status observer
    private fun observeFollowStatus(userid: String, btn: Button) {
        val currentId = currentUser?.uid ?: return
        val followRef = FirebaseDatabase.getInstance()
            .getReference("Follow")
            .child(currentId)
            .child("following")
            .child(userid)

        followRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                btn.text = if (snapshot.exists()) "Following" else "Follow"
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
