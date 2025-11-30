//ScheduleFragment.kt
package com.example.nenass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nenass.model.HarvestViewModel
import com.example.nenass.view.CropFormView

class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose the Composition when the view's LifecycleOwner is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                // Apply MaterialTheme for proper styling
                MaterialTheme {
                    HarvestScreen()
                }
            }
        }
    }
}

@Composable
fun HarvestScreen(viewModel: HarvestViewModel = viewModel()) {
    // Collect the crops flow from the ViewModel
    val crops by viewModel.crops.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // App Bar Title
        Text(
            text = "Harvest Calendar",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Instructions & Buttons
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { viewModel.addCrop() }) {
                Text("Add Crop")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* Save is usually auto-handled, but you can add a manual trigger here */ }) {
                Text("Save")
            }
        }

        // The Calendar Visualization
        // This renders the chart based on the crops list
        CalendarView(crops = crops)

        Spacer(modifier = Modifier.height(16.dp))

        // The List of Crop Forms
        // We use weight(1f) so this list takes up the remaining screen space and scrolls
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(crops) { crop ->
                CropFormView(
                    crop = crop,
                    onUpdate = { updatedCrop ->
                        viewModel.saveCrop(updatedCrop)
                    },
                    onDelete = {
                        // Safely handle nullable ID
                        crop.id?.let { id -> viewModel.deleteCrop(id) }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}