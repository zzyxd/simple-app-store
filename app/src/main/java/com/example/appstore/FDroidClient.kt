package com.example.appstore

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class FDroidClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    suspend fun fetchRepo(url: String): Result<FDroidRepo> = try {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Result.failure(Exception("HTTP ${response.code}"))
            } else {
                val body = response.body?.string() ?: ""
                val repo = gson.fromJson(body, FDroidRepo::class.java)
                Result.success(repo)
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getIconUrl(baseUrl: String, iconPath: String?): String? {
        if (iconPath.isNullOrBlank()) return null
        return if (iconPath.startsWith("http://") || iconPath.startsWith("https://")) {
            iconPath
        } else {
            val base = baseUrl.trimEnd('/')
            "$base/icons/$iconPath"
        }
    }
}
