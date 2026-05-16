package com.gramavasathi.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Activity
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.FavoritesRepository
import com.gramavasathi.data.repository.HomestayRepository
import com.gramavasathi.utils.FirebaseSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = HomestayRepository()
    private val seeder = FirebaseSeeder()
    val favRepo = FavoritesRepository(app)

    private val _all = MutableStateFlow<List<Homestay>>(emptyList())
    val all: StateFlow<List<Homestay>> = _all.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedActivity = MutableStateFlow<String?>(null)
    val selectedActivity = _selectedActivity.asStateFlow()

    val favorites = favRepo.favorites

    val activities = listOf(
        Activity("🐄", "Cow Milking"), Activity("🌾", "Field Plowing"), Activity("🍳", "Local Cooking"),
        Activity("🐦", "Bird Watching"), Activity("🌅", "Sunrise Trek"), Activity("🎣", "Fishing"),
        Activity("🌿", "Herb Garden"), Activity("🏞️", "Nature Walk")
    )

    fun load(onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _all.value = runCatching { repo.getAllHomestays() }.getOrDefault(emptyList())
            _isLoading.value = false
            onDone?.invoke()
        }
    }

    fun setActivity(name: String?) {
        _selectedActivity.value = if (_selectedActivity.value == name) null else name
    }

    fun filtered(): List<Homestay> {
        val a = _selectedActivity.value ?: return _all.value
        return _all.value.filter { it.activities.contains(a) }
    }

    fun toggleFavorite(id: String) { favRepo.toggle(id) }

    fun seed(onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = runCatching { seeder.seedAll() }.isSuccess
            onDone(ok)
            if (ok) load()
        }
    }
}
