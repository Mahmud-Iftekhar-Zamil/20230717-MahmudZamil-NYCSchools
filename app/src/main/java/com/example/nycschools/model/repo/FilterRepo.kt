package com.example.nycschools.model.repo

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FilterRepo"
private const val PREF_NAME = "Filter_DB"

enum class SortOrder { BY_ASC, BY_DESC }

private object PrefKeys {
    val SORT_ORDER = stringPreferencesKey("sort_order")
}

data class FilterPref(val sortOrder: SortOrder)

/**
 * Use datastore to store user's preferences such as sort schools based on Avg. SAT High or Low scores.
 * We can use this class to add more filter options, example: user can "bookmark" some schools and those
 * schools will always shows on top.
 * */

@Singleton
class FilterRepo @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)
    private val dataStore = context.dataStore

    val prefFlow = dataStore.data
        .catch {exception ->
            if(exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PrefKeys.SORT_ORDER] ?: SortOrder.BY_DESC.name
            )
            FilterPref(sortOrder)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit {preference ->
            preference[PrefKeys.SORT_ORDER] = sortOrder.name
        }
    }
    suspend fun getCurrentSortOrderFromDataStore(): String {
        val prefS = dataStore.data.first()
        return prefS[PrefKeys.SORT_ORDER].toString()
    }

}