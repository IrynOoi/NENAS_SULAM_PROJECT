//PopularAdapter
package com.example.nenass.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nenass.R
import com.example.nenass.model.PopularModel

class PopularAdapter(
    private val context: Context,
    private val popularList: List<PopularModel>
) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.popular_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = popularList[position]

        Glide.with(context).load(item.img_url).into(holder.popImg)
        holder.name.text = item.name
        holder.description.text = item.description
        holder.discount.text = item.discount

        // Convert Float rating to String for TextView
        holder.rating.text = item.rating.toString()

        // Update RatingBar
        holder.ratingBar.rating = item.rating
    }

    override fun getItemCount(): Int = popularList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val popImg: ImageView = itemView.findViewById(R.id.pop_img)
        val name: TextView = itemView.findViewById(R.id.label1)
        val description: TextView = itemView.findViewById(R.id.decpt)
        val discount: TextView = itemView.findViewById(R.id.promo_text)
        val rating: TextView = itemView.findViewById(R.id.rating_text)
        val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)
    }
}
