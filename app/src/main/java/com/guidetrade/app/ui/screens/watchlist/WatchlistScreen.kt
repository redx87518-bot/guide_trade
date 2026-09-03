package com.guidetrade.app.ui.screens.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.materialicons.Icons
import androidx.compose.materialicons.filled.Add
import androidx.compose.materialicons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel = viewModel(),
    onItemClicked: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.items.isEmpty()) {
            EmptyWatchlist(onAddClicked = { viewModel.addSymbol("AAPL", "Apple Inc.") })
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.items, key = { it.symbol }) { item ->
                    WatchlistItemCard(
                        item = item,
                        onItemClicked = { onItemClicked(item.symbol) },
                        onRemoveClicked = { viewModel.removeSymbol(item.symbol) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.addSymbol("TSLA", "Tesla Inc.") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add symbol")
        }
    }
}

@Composable
fun WatchlistItemCard(
    item: com.guidetrade.app.domain.model.WatchlistItem,
    onItemClicked: () -> Unit,
    onRemoveClicked: () -> Unit
) {
    ElevatedCard(
        onClick = onItemClicked,
        modifier = Modifier.fillMaxWidth().height(72.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.companyName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                item.lastPrice?.let {
                    Text(
                        text = "$${"%.2f".format(it)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if ((item.change ?: 0.0) >= 0) Color(0xFF4CAF50) else Color(0xFFFF5252),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            IconButton(onClick = onRemoveClicked) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EmptyWatchlist(onAddClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your watchlist is empty",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Add symbols to track market movements.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onAddClicked) {
            Text("Add Sample Symbol")
        }
    }
}
