//ShowList.kt
package com.example.nenass

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nenass.adapter.ShowAdapter
import com.example.nenass.model.UserModel
import com.google.firebase.database.*

class ShowList : AppCompatActivity() {

    private var id: String? = null
    private var title: String? = null

    private lateinit var titleTv: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShowAdapter

    private val idList = ArrayList<String>()
    private val usersList = ArrayList<UserModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_connection)

        titleTv = findViewById(R.id.title)

        intent?.let {
            id = it.getStringExtra("id")
            title = it.getStringExtra("title")
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        titleTv.text = title

        recyclerView = findViewById(R.id.recyclerView_connection)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = ShowAdapter(this, usersList)
        recyclerView.adapter = adapter

        when (title) {
            "Followers" -> getFollowers()
            "Following" -> getFollowing()
        }
    }

    private fun getFollowers() {
        val database =
            FirebaseDatabase.getInstance("https://nenass-c55f0-default-rtdb.asia-southeast1.firebasedatabase.app")
        val reference = database.getReference("Follow").child(id ?: return).child("followers")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                idList.clear()
                snapshot.children.forEach { it.key?.let { key -> idList.add(key) } }
                showUsers()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ShowList, "Error loading..", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getFollowing() {
        val database =
            FirebaseDatabase.getInstance("https://nenass-c55f0-default-rtdb.asia-southeast1.firebasedatabase.app")
        val reference = database.getReference("Follow").child(id ?: return).child("following")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                idList.clear()
                snapshot.children.forEach { it.key?.let { key -> idList.add(key) } }
                showUsers()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ShowList, "Error loading..", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showUsers() {
        val database =
            FirebaseDatabase.getInstance("https://nenass-c55f0-default-rtdb.asia-southeast1.firebasedatabase.app")
        val reference = database.getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                snapshot.children.forEach { snap ->
                    val user = snap.getValue(UserModel::class.java)
                    if (user != null && idList.contains(user.user_id)) {
                        usersList.add(user)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
