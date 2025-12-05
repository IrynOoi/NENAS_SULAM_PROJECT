////ViewAdapter.kt
//package com.example.nenass.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.nenass.model.ViewAllModel
//import com.example.nenass.R
//
//class ViewAllAdapter(
//    private val context: Context,
//    private val list: List<ViewAllModel>
//) : RecyclerView.Adapter<ViewAllAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.view_all_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = list[position]
//
//        Glide.with(context).load(item.img_url).into(holder.imageView)
//        holder.name.text = item.name
//        holder.description.text = item.description
//        holder.rating.text = item.rating
//
//        // Price logic based on type
//        holder.price.text = when (item.type) {
//            "egg" -> "${item.price}/dozen"
//            "milk" -> "${item.price}/litre"
//            else -> "${item.price}/kg"
//        }
//    }
//
//    override fun getItemCount(): Int = list.size
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.viewall_img)
//        val name: TextView = itemView.findViewById(R.id.viewall_name)
//        val description: TextView = itemView.findViewById(R.id.viewall_description)
//        val rating: TextView = itemView.findViewById(R.id.viewall_rating)
//        val price: TextView = itemView.findViewById(R.id.viewall_price)
//    }
//}
