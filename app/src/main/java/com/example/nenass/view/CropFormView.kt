//CropFormView
package com.example.nenass.view

import com.example.nenass.ui.theme.YellowPrimary
import com.example.nenass.ui.theme.OrangeAccent
import com.example.nenass.ui.theme.OrangeDark

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar
import com.example.nenass.model.Crop
import com.example.nenass.model.Season
import androidx.compose.ui.graphics.Color

@Composable
fun CropFormView(
    crop: Crop,
    onUpdate: (Crop) -> Unit,
    onSave: (Crop) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun showDatePicker(currentDate: String, onDateSelected: (String) -> Unit) {
        if (currentDate.isNotEmpty()) {
            try {
                val p = currentDate.split("-")
                if (p.size == 3) {
                    calendar.set(p[0].toInt(), p[1].toInt() - 1, p[2].toInt())
                }
            } catch (_: Exception) {
            }
        }

        DatePickerDialog(
            context,
            { _: DatePicker, year, month, day ->
                onDateSelected(String.format("%d-%02d-%02d", year, month + 1, day))
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
        colors = CardDefaults.cardColors(
            containerColor = YellowPrimary.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            OutlinedTextField(
                value = crop.name,
                onValueChange = { newValue ->
                    onUpdate(crop.copy(name = newValue))
                },
                label = { Text("Crop Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangeAccent,
                    focusedLabelColor = OrangeAccent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            crop.seasons.forEachIndexed { index, season ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            showDatePicker(season.start) { newDate ->
                                val newList = crop.seasons.toMutableList()
                                newList[index] = season.copy(start = newDate)
                                onUpdate(crop.copy(seasons = newList))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = YellowPrimary,
                            contentColor = Color.Black  // <-- black text
                        )
                    ) {
                        Text(if (season.start.isEmpty()) "Start Date" else season.start)
                    }

                    Button(
                        onClick = {
                            showDatePicker(season.end) { newDate ->
                                val newList = crop.seasons.toMutableList()
                                newList[index] = season.copy(end = newDate)
                                onUpdate(crop.copy(seasons = newList))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeAccent,
                            contentColor = Color.Black  // <-- black text
                        )
                    ) {
                        Text(if (season.end.isEmpty()) "End Date" else season.end)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        onUpdate(crop)
                        onSave(crop)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowPrimary,
                        contentColor = Color.Black // <-- black text
                    ),
                ) {
                    Text("Save")
                }

                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeDark,
                        contentColor = Color.Black // <-- black text
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
