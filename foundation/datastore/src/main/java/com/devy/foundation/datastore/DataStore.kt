package com.devy.foundation.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Created by JeYHoon on 2025. 10. 2..
 */

class DataStore(
    private val dataStore: DataStore<Preferences>
) {
    fun getStringDataFlow(name: String, defaultStr: String = ""): Flow<String> {
        return dataStore.data.map { prefs ->
            prefs[stringPreferencesKey(name)] ?: defaultStr
        }
    }

    suspend fun getStringData(name: String, default: String = ""): String {
        val preferences = dataStore.data.first()
        return preferences[stringPreferencesKey(name)] ?: default
    }

    suspend fun setStringData(name: String, value: String) {
        dataStore.edit {
            it[stringPreferencesKey(name)] = value
        }
    }

    fun getBooleanDataFlow(name: String, default: Boolean = false): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[booleanPreferencesKey(name)] ?: default
        }
    }

    suspend fun getBooleanData(name: String, default: Boolean = false): Boolean {
        val preferences = dataStore.data.first()
        return preferences[booleanPreferencesKey(name)] ?: default
    }

    suspend fun setBooleanData(name: String, value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(name)] = value
        }
    }

    fun getIntDataFlow(name: String): Flow<Int> {
        return dataStore.data.map { prefs ->
            prefs[intPreferencesKey(name)] ?: 0
        }
    }

    suspend fun getIntData(name: String): Int {
        val preferences = dataStore.data.first()
        return preferences[intPreferencesKey(name)] ?: 0
    }

    suspend fun setIntData(name: String, value: Int) {
        dataStore.edit {
            it[intPreferencesKey(name)] = value
        }
    }

    fun getLongDataFlow(name: String): Flow<Long> {
        return dataStore.data.map { prefs ->
            prefs[longPreferencesKey(name)] ?: 0L
        }
    }

    suspend fun getLongData(name: String, default: Long = 0L): Long {
        val preferences = dataStore.data.first()
        return preferences[longPreferencesKey(name)] ?: default
    }

    suspend fun setLongData(name: String, value: Long) {
        dataStore.edit {
            it[longPreferencesKey(name)] = value
        }
    }

    fun getFloatDataFlow(name: String): Flow<Float> {
        return dataStore.data.map { prefs ->
            prefs[floatPreferencesKey(name)] ?: 0f
        }
    }

    suspend fun getFloatData(name: String): Float {
        val preferences = dataStore.data.first()
        return preferences[floatPreferencesKey(name)] ?: 0f
    }

    suspend fun setFloatData(name: String, value: Float) {
        dataStore.edit {
            it[floatPreferencesKey(name)] = value
        }
    }

    fun getDoubleDataFlow(name: String): Flow<Double> {
        return dataStore.data.map { prefs ->
            prefs[doublePreferencesKey(name)] ?: 0.0
        }
    }

    suspend fun getDoubleData(name: String): Double {
        val preferences = dataStore.data.first()
        return preferences[doublePreferencesKey(name)] ?: 0.0
    }

    suspend fun setDoubleData(name: String, value: Double) {
        dataStore.edit {
            it[doublePreferencesKey(name)] = value
        }
    }

    fun getLongListDataFlow(name: String): Flow<List<Long>> {
        return dataStore.data.map { prefs ->
            val jsonString = prefs[stringPreferencesKey(name)] ?: ""
            if (jsonString.isEmpty()) {
                emptyList()
            } else {
                try {
                    Json.decodeFromString<List<Long>>(jsonString)
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }
    }

    suspend fun getLongListData(name: String): List<Long> {
        val preferences = dataStore.data.first()
        val jsonString = preferences[stringPreferencesKey(name)] ?: ""
        return if (jsonString.isEmpty()) {
            emptyList()
        } else {
            try {
                Json.decodeFromString<List<Long>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun setLongListData(name: String, value: List<Long>) {
        try {
            val jsonString = Json.encodeToString(value)
            dataStore.edit {
                it[stringPreferencesKey(name)] = jsonString
            }
        } catch (e: Exception) {
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    suspend fun all(): List<Pair<String, Any>> {
        return dataStore.data.map { prefs ->
            prefs.asMap().map { entry ->
                entry.key.name to entry.value
            }
        }.first()
    }
}