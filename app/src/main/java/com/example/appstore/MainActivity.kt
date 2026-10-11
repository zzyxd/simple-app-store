package com.example.appstore

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class AppItem(
    val name: String,
    val description: String,
    val category: String,
    val accent: Color,
    val emoji: String,
    val url: String,
)

@Composable
fun AppStoreScreen() {
    val context = LocalContext.current

    val apps = listOf(
        AppItem(
            name = "kDI",
            description = "A lightweight Android utility and system tool.",
            category = "Tools",
            accent = Color(0xFF5B7CFA),
            emoji = "🧰",
            url = "https://f-droid.org/archive/"
        ),
        AppItem(
            name = "System/App Mover",
            description = "Move apps between internal storage and SD card.",
            category = "System",
            accent = Color(0xFF14B8A6),
            emoji = "📦",
            url = "https://f-droid.org/archive/"
        ),
        AppItem(
            name = "0xBenchmark",
            description = "A minimalist benchmark utility for Android devices.",
            category = "Utilities",
            accent = Color(0xFFEAB308),
            emoji = "⚙️",
            url = "https://f-droid.org/archive/"
        ),
        AppItem(
            name = "10-bit Clock Widget",
            description = "A widget that shows time in a compact, stylish format.",
            category = "Widgets",
            accent = Color(0xFFEC4899),
            emoji = "🕒",
            url = "https://f-droid.org/archive/"
        ),
        AppItem(
            name = "10,000 Sentences",
            description = "A language-learning and sentence-memory app.",
            category = "Education",
            accent = Color(0xFF8B5CF6),
            emoji = "📚",
            url = "https://f-droid.org/archive/"
        )
    )

    var searchQuery by remember { mutableStateOf("") }

    val filteredApps = apps.filter {
        val q = searchQuery.trim().lowercase()
        q.isEmpty() || it.name.lowercase().contains(q) || it.category.lowercase().contains(q)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F7FB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "App Store",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Open-source apps from the F-Droid archive",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search apps...") },
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredApps) { app ->
                    AppCard(
                        item = app,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app.url))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppCard(
    item: AppItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(item.accent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.emoji,
                    fontSize = 28.sp
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
