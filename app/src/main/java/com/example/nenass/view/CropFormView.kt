//CropFormVIew.kt
package com.example.nenass.view // ✅ Correct package for the 'view' folder

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

// ✅ IMPORT YOUR DATA CLASSES EXPLICITLY
import com.example.nenass.model.Crop
import com.example.nenass.model.Season

@Composable
fun CropFormView(crop: Crop, onUpdate: (Crop) -> Unit, onDelete: () -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Helper function to show DatePicker
    fun showDatePicker(currentDate: String, onDateSelected: (String) -> Unit) {
        if (currentDate.isNotEmpty()) {
            try {
                val parts = currentDate.split("-")
                if (parts.size == 3) {
                    calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
                }
            } catch (e: Exception) {
                // Fallback to today if parsing fails
            }
        }

        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                // Format: YYYY-MM-DD
                val formattedDate = String.format("%d-%02d-%02d", year, month + 1, day)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Name Input
            OutlinedTextField(
                value = crop.name,
                onValueChange = { onUpdate(crop.copy(name = it)) },
                label = { Text("Crop Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Seasons List
            crop.seasons.forEachIndexed { index, season ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Start Date Button
                    Button(
                        onClick = {
                            showDatePicker(season.start) { newDate ->
                                val newSeasons = crop.seasons.toMutableList()
                                // Create a copy of the season with the new start date
                                newSeasons[index] = season.copy(start = newDate)
                                onUpdate(crop.copy(seasons = newSeasons))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        // Handle empty date string for display
                        val dateText = if (season.start.isEmpty()) "Start Date" else season.start
                        Text(dateText)
                    }

                    // End Date Button
                    Button(
                        onClick = {
                            showDatePicker(season.end) { newDate ->
                                val newSeasons = crop.seasons.toMutableList()
                                // Create a copy of the season with the new end date
                                newSeasons[index] = season.copy(end = newDate)
                                onUpdate(crop.copy(seasons = newSeasons))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        // Handle empty date string for display
                        val dateText = if (season.end.isEmpty()) "End Date" else season.end
                        Text(dateText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Delete Button
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete Crop")
            }
        }
    }
}