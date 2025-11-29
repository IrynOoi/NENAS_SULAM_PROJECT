// SearchAdapter.kt
package com.example.nenass

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nenass.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase

class SearchAdapter(
    private val context: Context,
    private val dataList: List<UserModel>
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile: ImageView = itemView.findViewById(R.id.profile_image)
        val username: TextView = itemView.findViewById(R.id.textView4)
        val memer: TextView = itemView.findViewById(R.id.Memer)
        val btnFollowOrFollowing: Button = itemView.findViewById(R.id.button2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.showlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        // Set user data safely
        holder.username.text = data.username ?: "Unknown"
        holder.memer.text = data.memer ?: ""
        data.profileUrl?.let { url ->
            Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.user_profile)
                .into(holder.profile)
        }

        // Navigate to OthersProfile on item click
        holder.itemView.setOnClickListener {
            data.user_id?.let { uid ->
                val intent = Intent(context, OthersProfile::class.java)
                intent.putExtra("uid", uid)
                context.startActivity(intent)
            }
        }

        // Hide follow button for current user
        if (currentUser?.uid == data.user_id) {
            holder.btnFollowOrFollowing.visibility = View.GONE
        } else {
            holder.btnFollowOrFollowing.visibility = View.VISIBLE
            data.user_id?.let { userid ->
                updateFollowButtonText(userid, holder.btnFollowOrFollowing)
            }
        }

        // Handle follow/unfollow button click
        holder.btnFollowOrFollowing.setOnClickListener {
            val buttonText = holder.btnFollowOrFollowing.text.toString()
            val targetId = data.user_id ?: return@setOnClickListener
            val currentId = currentUser?.uid ?: return@setOnClickListener

            val followRef = FirebaseDatabase.getInstance().getReference("Follow")

            if (buttonText == "Follow") {
                // FOLLOW
                followRef.child(currentId).child("following").child(targetId).setValue(true)
                followRef.child(targetId).child("followers").child(currentId).setValue(true)
                addNotification(targetId)
            } else if (buttonText == "Following") {
                // UNFOLLOW
                followRef.child(currentId).child("following").child(targetId).removeValue()
                followRef.child(targetId).child("followers").child(currentId).removeValue()
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    /** Update button text based on follow status */
    private fun updateFollowButtonText(userid: String, btn: Button) {
        val currentId = currentUser?.uid ?: return
        val followRef = FirebaseDatabase.getInstance().getReference("Follow")
            .child(currentId)
            .child("following")

        followRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                btn.text = if (snapshot.child(userid).exists()) "Following" else "Follow"
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /** Add a notification entry for the target user */
    private fun addNotification(userid: String) {
        val currentId = currentUser?.uid ?: return
        val notifRef = FirebaseDatabase.getInstance().getReference("Notifications").child(userid)

        val map = hashMapOf(
            "userid" to currentId,
            "comment" to "started following you",
            "postid" to "",
            "ispost" to false
        )
        notifRef.push().setValue(map)
    }
}
