////DetailedActivity.kt
//package com.example.nenass
//
//import android.R
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import com.bumptech.glide.Glide
//
//class DetailedActivity : AppCompatActivity() {
//    var detailedImg: ImageView? = null
//    var addItem: ImageView? = null
//    var removeItem: ImageView? = null
//    var price: TextView? = null
//    var rating: TextView? = null
//    var description: TextView? = null
//    var addToCart: Button? = null
//    var toolbar: Toolbar? = null
//    var viewAllModel: ViewAllModel? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detailed)
//
//        // ------------------ Toolbar Setup ------------------
//        toolbar = findViewById<Toolbar?>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//
//        // ------------------ Get Object from Intent ------------------
//        val `object`: Any? = intent.getSerializableExtra("detail")
//
//        if (`object` is ViewAllModel) {
//            viewAllModel = `object` as ViewAllModel?
//        } else {
//            Log.e("DetailedActivity", "Invalid model passed through Intent")
//            return
//        }
//
//        // ------------------ Initialize UI ------------------
//        detailedImg = findViewById<ImageView?>(R.id.detailed_img)
//        addItem = findViewById<ImageView?>(R.id.add_item)
//        removeItem = findViewById<ImageView?>(R.id.remove_item)
//
//        price = findViewById<TextView?>(R.id.detailed_price)
//        rating = findViewById<TextView?>(R.id.detailed_rating)
//        description = findViewById<TextView?>(R.id.detailed_dec)
//
//        addToCart = findViewById<Button?>(R.id.add_to_cart)
//
//        // ------------------ Populate Data ------------------
//        Glide.with(this)
//            .load(viewAllModel.getImg_url())
//            .into(detailedImg)
//
//        rating.setText(viewAllModel.getRating())
//        description.setText(viewAllModel.getDescription())
//        price!!.text = "Price: $" + viewAllModel.getPrice() + "/kg"
//    }
//}