package ai.tchek.tcheksdksample

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sample")

private val previousScansKey = stringPreferencesKey("PREVIOUS_SCANS")

val DataStore<Preferences>.previousScansFlow: Flow<List<SampleTchekScan>>
    get() = data.map {
        it[previousScansKey]
    }.map { serialized ->
        serialized?.let {
            tryOrNull { Json.decodeFromString(serialized) }
        } ?: emptyList()
    }

suspend fun DataStore<Preferences>.saveSampleScans(sampleTchekScans: List<SampleTchekScan>) = edit {
    val serialized = Json.encodeToString(sampleTchekScans)
    it[previousScansKey] = serialized
}

private inline fun <T> tryOrNull(tryBlock: () -> T): T? =
    try {
        tryBlock.invoke()
    } catch (exception: Exception) {
        null
    }
