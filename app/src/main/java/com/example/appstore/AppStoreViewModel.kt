package com.example.appstore

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class AppUiModel(
    val packageId: String,
    val name: String,
    val summary: String,
    val category: String,
    val iconUrl: String?,
    val license: String,
    val latestVersion: String?,
    val appSize: String?
)

class AppStoreViewModel : ViewModel() {
    private val fdroidClient = FDroidClient()

    val apps: MutableState<List<AppUiModel>> = mutableStateOf(emptyList())
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val errorMessage: MutableState<String?> = mutableStateOf(null)
    val searchQuery: MutableState<String> = mutableStateOf("")

    fun loadApps() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            errorMessage.value = null

            val result = fdroidClient.fetchRepo("https://f-droid.org/repo/index-v1.json")

            result.onSuccess { repo ->
                val appList = repo.packages
                    .map { (packageId, pkg) ->
                        val metadata = pkg.metadata
                        val version = pkg.versions[pkg.suggestedVersionCode.toString()]
                            ?: pkg.versions.values.firstOrNull()

                        val iconUrl = when {
                            metadata.icon.isNullOrBlank() -> null
                            metadata.icon.startsWith("http://") || metadata.icon.startsWith("https://") -> metadata.icon
                            else -> fdroidClient.getIconUrl("https://f-droid.org/repo", metadata.icon)
                        }

                        val category = pkg.categories.firstOrNull()?.ifBlank { "Other" } ?: "Other"
                        val latestVersion = version?.versionName ?: "unknown"
                        val appSize = version?.apkSize?.let { formatBytes(it) }

                        AppUiModel(
                            packageId = packageId,
                            name = metadata.name.ifBlank { packageId },
                            summary = metadata.summary.ifBlank { "Open-source Android app" },
                            category = category,
                            iconUrl = iconUrl,
                            license = metadata.license.ifBlank { "Unknown" },
                            latestVersion = latestVersion,
                            appSize = appSize
                        )
                    }
                    .sortedBy { it.name.lowercase() }

                apps.value = appList
                isLoading.value = false
            }

            result.onFailure { error ->
                errorMessage.value = "Failed to load apps: ${error.message ?: "unknown error"}"
                isLoading.value = false
            }
        }
    }

    fun getFilteredApps(): List<AppUiModel> {
        val query = searchQuery.value.trim().lowercase()
        return if (query.isEmpty()) {
            apps.value
        } else {
            apps.value.filter {
                it.name.lowercase().contains(query) ||
                it.summary.lowercase().contains(query) ||
                it.category.lowercase().contains(query)
            }
        }
    }

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
            bytes >= 1024 -> String.format("%.1f KB", bytes / 1024.0)
            else -> "$bytes B"
        }
    }
}
