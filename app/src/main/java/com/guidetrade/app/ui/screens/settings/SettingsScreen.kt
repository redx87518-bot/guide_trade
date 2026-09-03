package com.guidetrade.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.VolumeUp

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    onSignOut: () -> Unit = {},
    onVoiceSettingsClicked: () -> Unit = {},
    onTelegramSettingsClicked: () -> Unit = {},
    onDiscordSettingsClicked: () -> Unit = {},
    onNotificationsSettingsClicked: () -> Unit = {},
    onHistoryClicked: () -> Unit = {},
    onReportsClicked: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SettingsSectionHeader("Account")
        }
        item {
            SettingItem(
                icon = Icons.Default.Person,
                title = "Profile",
                subtitle = uiState.settings?.uid ?: "",
                onClick = { }
            )
        }
        item {
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
        }
        item {
            SettingItem(
                icon = Icons.Default.ExitToApp,
                title = "Sign Out",
                onClick = { onSignOut() }
            )
        }

        item {
            SettingsSectionHeader("Voice")
        }
        item {
            SettingSwitchItem(
                title = "Voice Input",
                subtitle = "Enable voice input for research queries",
                checked = uiState.settings?.voiceEnabled ?: false,
                onCheckedChange = { }
            )
        }
        item {
            SettingSwitchItem(
                title = "Auto-read Research",
                subtitle = "Read research summaries aloud",
                checked = uiState.settings?.autoReadResearch ?: false,
                onCheckedChange = { }
            )
        }
        item {
            SettingItem(
                icon = Icons.Default.VolumeUp,
                title = "Voice Settings",
                onClick = onVoiceSettingsClicked
            )
        }

        item {
            SettingsSectionHeader("Notifications")
        }
        item {
            SettingSwitchItem(
                title = "Research Completed",
                subtitle = "Notify when research is complete",
                checked = true,
                onCheckedChange = { }
            )
        }
        item {
            SettingItem(
                icon = Icons.Default.Notifications,
                title = "Notification Settings",
                onClick = onNotificationsSettingsClicked
            )
        }

        item {
            SettingsSectionHeader("Integrations")
        }
        item {
            SettingItem(
                icon = Icons.Default.Notifications,
                title = "Telegram Settings",
                subtitle = "Configure Telegram notifications",
                onClick = onTelegramSettingsClicked
            )
        }
        item {
            SettingItem(
                icon = Icons.Default.Notifications,
                title = "Discord Settings",
                subtitle = "Configure Discord notifications",
                onClick = onDiscordSettingsClicked
            )
        }

        item {
            SettingsSectionHeader("History & Reports")
        }
        item {
            SettingItem(
                icon = Icons.Default.History,
                title = "History",
                onClick = onHistoryClicked
            )
        }
        item {
            SettingItem(
                icon = Icons.Default.Report,
                title = "Reports",
                onClick = onReportsClicked
            )
        }

        item {
            SettingsSectionHeader("Preferences")
        }
        item {
            SettingItem(
                title = "Research Style",
                subtitle = "Default research style",
                onClick = { }
            )
        }
        item {
            SettingItem(
                title = "Default Market",
                subtitle = "US",
                onClick = { }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
    )
}

@Composable
fun SettingItem(
    icon: ImageVector? = null,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (subtitle != null) 64.dp else 56.dp)
                .padding(12.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            icon?.let {
                Icon(imageVector = it, contentDescription = null)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SettingSwitchItem(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (subtitle != null) 64.dp else 56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors()
        )
    }
}
