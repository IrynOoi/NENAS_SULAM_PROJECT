//ScheduleViewModel.kt
package com.example.nenass

import com.example.nenass.Crop
import com.example.nenass.Season
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ScheduleViewModel : ViewModel() {

    // Explicitly specify type: MutableList<Crop>
    var crops by mutableStateOf(mutableListOf<Crop>())
        private set

    fun updateCrop(index: Int, crop: Crop) {
        val newList = crops.toMutableList()
        newList[index] = crop
        crops = newList
    }

    fun deleteCrop(index: Int) {
        val newList = crops.toMutableList()
        newList.removeAt(index)
        crops = newList
    }

    fun addSeason(index: Int) {
        val newList = crops.toMutableList()
        val crop = newList[index]
        crop.seasons.add(Season())
        newList[index] = crop
        crops = newList
    }

    fun addCrop() {
        val newList = crops.toMutableList()
        newList.add(Crop())
        crops = newList
    }

}
