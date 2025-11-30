//CalendarView.kt
package com.example.nenass

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.nenass.model.Crop

@Composable
fun CalendarView(crops: List<Crop>) {
    // Configuration constants
    val months =
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val rowHeight = 100f

    // Calculate total height based on number of crops
    val totalHeight = (crops.size * rowHeight).dp.coerceAtLeast(200.dp)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalHeight)
    ) {
        val gridUnit = size.width / 12

        // 1. Draw Background Grid & Month Labels
        months.forEachIndexed { index, month ->
            val xPos = index * gridUnit

            // Draw vertical grid lines
            drawLine(
                color = Color.LightGray,
                start = Offset(xPos, 0f),
                end = Offset(xPos, size.height)
            )

            // Draw month labels (using Android native Canvas for text)
            drawContext.canvas.nativeCanvas.drawText(
                month,
                xPos + 10f,
                30f,
                Paint().apply { textSize = 30f }
            )
        }

        // 2. Draw Crops
        crops.forEachIndexed { index, crop ->
            val yPos = index * rowHeight + 60f // Offset for header

            // Draw Crop Name
            drawContext.canvas.nativeCanvas.drawText(
                crop.name,
                10f,
                yPos + 40f,
                Paint().apply { textSize = 40f; isFakeBoldText = true }
            )

            // Draw Seasons
            crop.seasons.forEach { season ->
                val startX = calculateXFromDate(season.start, gridUnit)
                val endX = calculateXFromDate(season.end, gridUnit)
                val width = endX - startX

                if (width > 0) {
                    drawRect(
                        color = Color(0xFFFFC107), // Yellow-Orange (Amber)
                        topLeft = Offset(startX, yPos),
                        size = Size(width, 35f)
                    )

                }
            }
        }
    }
}

// Helper Logic translated from your original React App.js
fun calculateXFromDate(dateStr: String, gridUnit: Float): Float {
    if (dateStr.isEmpty()) return 0f

    try {
        // format: YYYY-MM-DD
        val parts = dateStr.split("-")
        if (parts.size < 3) return 0f

        val month = parts[1].toFloat()
        val day = parts[2].toFloat()

        // Logic from your original React App:
        // If day >= 20 -> use full month index
        // If day > 10 -> use month - 0.5
        // If day <= 10 -> use month - 1 (start of month)

        val monthIndex = when {
            day >= 20 -> month
            day > 10 -> month - 0.5f
            else -> month - 1f
        }

        return monthIndex * gridUnit
    } catch (e: Exception) {
        return 0f
    }
}