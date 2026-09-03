package com.guidetrade.app.ui.screens.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.materialicons.Icons
import androidx.compose.materialicons.filled.Chat
import androidx.compose.materialicons.filled.History
import androidx.compose.materialicons.filled.Mic
import androidx.compose.materialicons.filled.Search
import androidx.compose.materialicons.filled.Settings
import androidx.compose.materialicons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guidetrade.app.ui.theme.components.OrbAnimation
import com.guidetrade.app.ui.theme.components.OrbState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToChat: () -> Unit,
    onNavigateToResearch: () -> Unit,
    onNavigateToWatchlist: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        OrbAnimation(
            state = OrbState.Idle,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Guide Trade",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Your AI trading research assistant",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        VoiceToggle(
            voiceEnabled = uiState.settings?.voiceEnabled ?: false,
            onToggle = { }
        )
        Spacer(modifier = Modifier.height(24.dp))

        QuickActionCard(
            icon = Icons.Default.Search,
            title = "Research a Company",
            subtitle = "Ask about stocks, compare assets, analyze catalysts",
            onClick = onNavigateToResearch
        )
        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            icon = Icons.Default.Chat,
            title = "New Conversation",
            subtitle = "Start a chat about the market",
            onClick = onNavigateToChat
        )
        Spacer(modifier = Modifier.height(24.dp))

        QuickActionsRow(
            items = listOf(
                QuickNavItem("Watchlist", Icons.Default.Star, onNavigateToWatchlist),
                QuickNavItem("History", Icons.Default.History, { }),
                QuickNavItem("Settings", Icons.Default.Settings, onNavigateToSettings)
            )
        )
    }
}

@Composable
fun VoiceToggle(voiceEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = null,
            tint = if (voiceEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Voice",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = voiceEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors()
        )
    }
}

data class QuickNavItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun QuickActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun QuickActionsRow(items: List<QuickNavItem>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            androidx.compose.material3.Surface(
                onClick = item.onClick,
                shape = CircleShape,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp, 12.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
