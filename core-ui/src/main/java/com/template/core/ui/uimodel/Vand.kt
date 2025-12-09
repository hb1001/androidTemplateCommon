package com.template.core.ui.uimodel

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// --- 数据模型 ---
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Province(val code: String, val name: String)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class City(val code: String, val name: String, val provinceCode: String)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Area(val code: String, val name: String, val cityCode: String, val provinceCode: String)

val json = Json { ignoreUnknownKeys = true }

// --- 数据加载工具 ---
inline fun <reified T> Context.loadJsonList(path: String): List<T> {
    return try {
        val jsonStr = assets.open(path).bufferedReader().use { it.readText() }
        json.decodeFromString<List<T>>(jsonStr)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

// 简单的 Repository 模式，用于在 Compose 中异步获取数据
class AreaRepository(private val context: Context) {
    suspend fun getProvinces(): List<Province> = withContext(Dispatchers.IO) {
        context.loadJsonList("area/provinces.json")
    }
    suspend fun getCities(): List<City> = withContext(Dispatchers.IO) {
        context.loadJsonList("area/cities.json")
    }
    suspend fun getAreas(): List<Area> = withContext(Dispatchers.IO) {
        context.loadJsonList("area/areas.json")
    }
}
