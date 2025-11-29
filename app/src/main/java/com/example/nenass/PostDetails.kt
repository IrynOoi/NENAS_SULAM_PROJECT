//PostDetails.kt
package com.example.nenass
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.nenass.adapter.PostAdapter
//import com.example.nenass.model.Posts
//import com.google.firebase.database.*
//
//class PostDetails : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var postsList: MutableList<Posts>
//    private lateinit var adapter: PostAdapter
//
//    private var postId: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_post_details)
//
//        // Initialize RecyclerView
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // Initialize list and adapter
//        postsList = mutableListOf()
//        adapter = PostAdapter(this, postsList)
//        recyclerView.adapter = adapter
//
//        // Get postId from intent safely
//        postId = intent.getStringExtra("postid")
//
//        postId?.let {
//            readPosts(it)
//        } ?: run {
//            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun readPosts(postId: String) {
//        val reference: DatabaseReference = FirebaseDatabase.getInstance()
//            .getReference("Posts")
//            .child(postId)
//
//        reference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                postsList.clear()
//                val post: Posts? = snapshot.getValue(Posts::class.java)
//                post?.let { postsList.add(it) }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@PostDetails, "Failed to load post", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}
