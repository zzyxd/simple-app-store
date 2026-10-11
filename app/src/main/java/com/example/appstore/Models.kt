package com.example.appstore

data class FDroidRepo(
    val repo: RepoMetadata,
    val packages: Map<String, AppPackage>
)

data class RepoMetadata(
    val address: String,
    val timestamp: Long,
    val name: String? = null
)

data class AppPackage(
    val metadata: AppMetadata,
    val suggestedVersionCode: Int,
    val categories: List<String> = emptyList(),
    val versions: Map<String, AppVersion> = emptyMap()
)

data class AppMetadata(
    val name: String,
    val summary: String,
    val description: String = "",
    val icon: String? = null,
    val license: String = "Unknown",
    val authorEmail: String? = null,
    val webSite: String? = null
)

data class AppVersion(
    val versionName: String,
    val versionCode: Int,
    val apkName: String,
    val apkSize: Long,
    val minSdkVersion: Int? = null,
    val targetSdkVersion: Int? = null,
    val hash: String? = null,
    val added: Long? = null
)
