package com.gramavasathi.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.FavoritesRepository
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class DetailViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = HomestayRepository()
    val favRepo = FavoritesRepository(app)

    private val _homestay = MutableStateFlow<Homestay?>(null)
    val homestay: StateFlow<Homestay?> = _homestay.asStateFlow()

    val isFavorite: StateFlow<Boolean> = favRepo.favorites.map {
        _homestay.value?.let { h -> it.contains(h.id) } ?: false
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun load(id: String) {
        viewModelScope.launch {
            _homestay.value = runCatching { repo.getHomestayById(id) }.getOrNull()
        }
    }

    fun toggleFavorite() {
        _homestay.value?.let { favRepo.toggle(it.id) }
    }
}
