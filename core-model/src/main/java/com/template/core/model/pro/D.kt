package com.template.core.model.pro

import kotlinx.serialization.Serializable
@Serializable
data class Province(
    val code: String,
    val name: String
)

@Serializable
data class City(
    val code: String,
    val name: String,
    val provinceCode: String
)

@Serializable
data class Area(
    val code: String,
    val name: String,
    val cityCode: String,
    val provinceCode: String
)


inline fun <reified T> Context.loadJsonList(path: String, serializer: ListSerializer<T>): List<T> {
    val jsonStr = assets.open(path).bufferedReader().use { it.readText() }
    return Json.decodeFromString(serializer, jsonStr)
}