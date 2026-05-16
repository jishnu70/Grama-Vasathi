package com.gramavasathi.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("gv_favorites", Context.MODE_PRIVATE)

    private val _favorites = MutableStateFlow(loadFavorites())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private fun loadFavorites(): Set<String> =
        prefs.getStringSet("ids", emptySet()) ?: emptySet()

    fun isFavorite(id: String): Boolean = _favorites.value.contains(id)

    fun toggle(id: String) {
        val updated = _favorites.value.toMutableSet().apply {
            if (contains(id)) remove(id) else add(id)
        }
        _favorites.value = updated
        prefs.edit { putStringSet("ids", updated) }
    }
}
