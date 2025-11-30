//ScheduleFragment.kt
package com.example.nenass

import androidx.compose.material3.ButtonDefaults
import com.example.nenass.ui.theme.YellowPrimary
import com.example.nenass.ui.theme.OrangeAccent
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Alignment

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarvestScreen(viewModel: HarvestViewModel = viewModel()) {
    val crops by viewModel.crops.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Harvest Calendar",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = OrangeAccent
                )
            )

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // "Add Crop" button
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.addCrop() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowPrimary,
                        contentColor = androidx.compose.ui.graphics.Color.Black // text color
                    )
                ) {
                    Text("Add Crop")
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar visualization
            CalendarView(crops = crops)

            Spacer(modifier = Modifier.height(16.dp))

            // List of crop forms
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(crops) { crop ->
                    CropFormView(
                        crop = crop,
                        onUpdate = { updatedCrop -> viewModel.updateLocalCrop(updatedCrop) },
                        onSave = { cropToSave -> viewModel.saveCrop(cropToSave) },
                        onDelete = { crop.id?.let { id -> viewModel.deleteCrop(id) } }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}