//ConnectionFragment.kt
package com.example.nenass

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nenass.adapter.ShowAdapter
import com.example.nenass.model.UserModel
import com.google.firebase.database.*

class ConnectionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShowAdapter
    private val userList = ArrayList<UserModel>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_connection, container, false)

        recyclerView = view.findViewById(R.id.recyclerView_connection)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ShowAdapter(requireContext(), userList)
        recyclerView.adapter = adapter

        loadUsers()

        return view
    }

    private fun loadUsers() {
        // ✅ Explicit Singapore DB URL
        val database =
            FirebaseDatabase.getInstance("https://nenass-c55f0-default-rtdb.asia-southeast1.firebasedatabase.app")
        val ref = database.getReference("Users")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnap in snapshot.children) {
                    val user = userSnap.getValue(UserModel::class.java)
                    if (user != null) userList.add(user)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
