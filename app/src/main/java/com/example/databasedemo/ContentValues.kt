package com.example.databasedemo

import android.content.ContentValues

fun contentValuesOf(vararg pairs: Pair<String, Any?>) = ContentValues().apply {
    for (p in pairs) {
        val key = p.first
        when (val value = p.second) {
            is Int -> put(key, value)
            is Long -> put(key, value)
            is Short -> put(key, value)
            is Float -> put(key, value)
            is Double -> put(key, value)
            is Boolean -> put(key, value)
            is String -> put(key, value)
            is Byte -> put(key, value)
            is ByteArray -> put(key, value)
            null -> putNull(key)
        }
    }
}



