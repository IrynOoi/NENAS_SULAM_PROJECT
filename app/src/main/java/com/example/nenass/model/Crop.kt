// Crop.kt
package com.example.nenass.model


data class Crop(
    var id: String = "",
    var userId: String = "",
    var name: String = "",
    var seasons: MutableList<Season> = mutableListOf()
)