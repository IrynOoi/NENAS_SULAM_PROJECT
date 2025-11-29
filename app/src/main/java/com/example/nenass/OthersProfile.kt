//OthersProfile.kt
package com.example.nenass

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class OthersProfile : AppCompatActivity() {

    private lateinit var btnFollow: Button
    private lateinit var btnFollowing: Button
    private lateinit var profile: ImageView
    private lateinit var bg: ImageView
    private lateinit var username: TextView
    private lateinit var memer: TextView
    private lateinit var followingCount: TextView
    private lateinit var followersCount: TextView
    private lateinit var followers: TextView
    private lateinit var following: TextView
    private lateinit var toolbar: Toolbar

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        initViews()

        // Firebase setup
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users")

        // Toolbar back button
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // Get UID from intent
        id = intent.getStringExtra("uid") ?: ""

        // Show follow button or settings
        if (id == user.uid) {
            btnFollow.visibility = Button.VISIBLE
            btnFollow.text = "Settings"
            btnFollowing.visibility = Button.GONE
        } else {
            checkFollow()
        }

        // Load user data and counts
        getUserData()
        getFollowCount()

        // Followers/following click to navigate to ShowList
        followers.setOnClickListener {
            val intent = Intent(this, ShowList::class.java)
            intent.putExtra("id", id)
            intent.putExtra("title", "Followers")
            startActivity(intent)
        }

        following.setOnClickListener {
            val intent = Intent(this, ShowList::class.java)
            intent.putExtra("id", id)
            intent.putExtra("title", "Following")
            startActivity(intent)
        }

        // Follow/unfollow button clicks
        btnFollow.setOnClickListener {
            if (btnFollow.text.toString() == "Follow") {
                followUser()
            }
        }

        btnFollowing.setOnClickListener {
            if (btnFollowing.text.toString() == "Following") {
                unfollowUser()
            }
        }
    }

    private fun initViews() {
        btnFollow = findViewById(R.id.btn_follow)
        btnFollowing = findViewById(R.id.btn_following)
        profile = findViewById(R.id.profile_image)
        username = findViewById(R.id.username)
        memer = findViewById(R.id.memer)
        bg = findViewById(R.id.background)
        followingCount = findViewById(R.id.following_count)
        followersCount = findViewById(R.id.followers_count)
        followers = findViewById(R.id.followers)
        following = findViewById(R.id.following)
    }

    private fun getUserData() {
        reference.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val n = snapshot.child("username").value.toString()
                val m = snapshot.child("memer").value.toString()
                val p = snapshot.child("profileUrl").value.toString()
                val b = snapshot.child("background").value.toString()

                username.text = n
                memer.text = m

                Glide.with(this@OthersProfile)
                    .load(p)
                    .placeholder(R.drawable.user_profile)
                    .into(profile)

                Glide.with(this@OthersProfile)
                    .load(b)
                    .into(bg)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OthersProfile, "Error loading profile", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getFollowCount() {
        FirebaseDatabase.getInstance().getReference("Follow")
            .child(id)
            .child("followers")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    followersCount.text = snapshot.childrenCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        FirebaseDatabase.getInstance().getReference("Follow")
            .child(id)
            .child("following")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    followingCount.text = snapshot.childrenCount.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun checkFollow() {
        FirebaseDatabase.getInstance().getReference("Follow")
            .child(user.uid)
            .child("following")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(id).exists()) {
                        btnFollow.visibility = Button.GONE
                        btnFollowing.visibility = Button.VISIBLE
                    } else {
                        btnFollowing.visibility = Button.GONE
                        btnFollow.visibility = Button.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun followUser() {
        FirebaseDatabase.getInstance().getReference("Follow")
            .child(user.uid)
            .child("following")
            .child(id).setValue(true)

        FirebaseDatabase.getInstance().getReference("Follow")
            .child(id)
            .child("followers")
            .child(user.uid).setValue(true)

        addNotification()
    }

    private fun unfollowUser() {
        FirebaseDatabase.getInstance().getReference("Follow")
            .child(user.uid)
            .child("following")
            .child(id).removeValue()

        FirebaseDatabase.getInstance().getReference("Follow")
            .child(id)
            .child("followers")
            .child(user.uid).removeValue()
    }

    private fun addNotification() {
        val notifRef = FirebaseDatabase.getInstance().getReference("Notifications").child(id)
        val map = hashMapOf(
            "userid" to user.uid,
            "comment" to "started following you",
            "postid" to "",
            "ispost" to false
        )
        notifRef.push().setValue(map)
    }
}
