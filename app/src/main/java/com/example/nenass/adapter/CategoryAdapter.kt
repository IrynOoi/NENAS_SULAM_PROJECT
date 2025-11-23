//CategoryAdapter.kt
package com.example.nenass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nenass.R
import com.example.nenass.model.CategoryModel

class CategoryAdapter(
    private val context: Context,
    private val categoryList: List<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categoryList[position]

        holder.categoryName.text = item.name

        Glide.with(context)
            .load(item.img_url)
            .placeholder(R.drawable.cannedp) // Optional
            .into(holder.categoryImg)
    }

    override fun getItemCount(): Int = categoryList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImg: ImageView = itemView.findViewById(R.id.pop_img)
        val categoryName: TextView = itemView.findViewById(R.id.category_name)
    }
}
