package com.guidetrade.app.ui.screens.research

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ResearchScreen(
    viewModel: ResearchViewModel = viewModel(),
    onResearchStarted: (String) -> Unit = {},
    onResearchComplete: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        if (uiState.isResearching) {
            ResearchProgress(
                query = uiState.query,
                progress = uiState.progress,
                stage = uiState.progressStage
            )
        } else {
            ResearchInput(
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = { viewModel.startResearch() },
                isEnabled = !uiState.isResearching
            )

            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ResearchInput(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isEnabled: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "What would you like to research?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Enter a company name, stock symbol, or market question.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Research query") },
            placeholder = { Text("e.g., Research NVDA and explain recent catalysts") },
            enabled = isEnabled,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )

        Button(
            onClick = onSearch,
            enabled = isEnabled && query.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Research")
        }

        ElevatedCard(
            onClick = { onQueryChange("Research NVIDIA") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Research NVIDIA",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )
        }

        ElevatedCard(
            onClick = { onQueryChange("Compare Microsoft and Google") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Compare Microsoft and Google",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )
        }

        ElevatedCard(
            onClick = { onQueryChange("Why did this stock move?") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Why did this stock move?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ResearchProgress(
    query: String,
    progress: Float,
    stage: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Researching: $query",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            stroke = 4.dp
        )

        Text(
            text = stage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
