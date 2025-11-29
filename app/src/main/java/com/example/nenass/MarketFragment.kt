//MarketFragment.kt
package com.example.nenass

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nenass.adapter.CategoryAdapter
import com.example.nenass.adapter.PopularAdapter
import com.example.nenass.model.CategoryModel
import com.example.nenass.model.PopularModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class MarketFragment : Fragment() {

    private lateinit var popularRec: RecyclerView
    private lateinit var popularModelList: MutableList<PopularModel>
    private lateinit var popularAdapter: PopularAdapter

    private lateinit var categoryRec: RecyclerView
    private lateinit var categoryList: MutableList<CategoryModel>
    private lateinit var categoryAdapter: CategoryAdapter

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "MarketFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_marketplace, container, false)

        // ---------------- POPULAR RECYCLER ----------------
        popularRec = root.findViewById(R.id.pop_rec)
        popularRec.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        popularModelList = mutableListOf()
        popularAdapter = PopularAdapter(requireContext(), popularModelList)
        popularRec.adapter = popularAdapter

        fetchPopularProducts()

        // ---------------- CATEGORY RECYCLER ----------------
        categoryRec = root.findViewById(R.id.category_rec)
        categoryRec.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        categoryList = mutableListOf()
        categoryAdapter = CategoryAdapter(requireContext(), categoryList)
        categoryRec.adapter = categoryAdapter

        fetchCategories()

        return root
    }

    // ======================== FIRESTORE FUNCTIONS ========================

    private fun fetchCategories() {
        db.collection("category")
            .get()
            .addOnSuccessListener { result ->
                categoryList.clear()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val imgUrl = document.getString("img_url") ?: ""

                    categoryList.add(CategoryModel(imgUrl, name))
                }
                categoryAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e(TAG, "Error fetching categories", it)
            }
    }

    private fun fetchPopularProducts() {
        db.collection("PopularProduct")
            .get()
            .addOnSuccessListener { result ->
                popularModelList.clear()
                for (document: QueryDocumentSnapshot in result) {
                    val item = PopularModel(
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        rating = (document.get("rating") as? Number)?.toFloat() ?: 0f,
                        discount = document.getString("discount") ?: "",
                        type = document.getString("type") ?: "",
                        img_url = document.getString("img_url") ?: ""
                    )
                    popularModelList.add(item)
                }
                popularAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}
