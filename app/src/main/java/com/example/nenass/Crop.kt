//Crop.kt
package com.example.nenass

data class Crop(
    var id: String? = null,
    var name: String = "",
    var seasons: MutableList<Season> = mutableListOf(Season())
)