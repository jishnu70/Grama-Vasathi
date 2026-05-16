package com.gramavasathi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Activity
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import com.gramavasathi.utils.FirebaseSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repo = HomestayRepository()
    private val seeder = FirebaseSeeder()

    private val _all = MutableStateFlow<List<Homestay>>(emptyList())
    val all: StateFlow<List<Homestay>> = _all.asStateFlow()

    private val _selectedActivity = MutableStateFlow<String?>(null)
    val selectedActivity = _selectedActivity.asStateFlow()

    val activities = listOf(
        Activity("🐄", "Cow Milking"), Activity("🌾", "Field Plowing"), Activity("🍳", "Local Cooking"),
        Activity("🐦", "Bird Watching"), Activity("🌅", "Sunrise Trek"), Activity("🎣", "Fishing"),
        Activity("🌿", "Herb Garden"), Activity("🏞️", "Nature Walk")
    )

    fun load() {
        viewModelScope.launch {
            _all.value = runCatching { repo.getAllHomestays() }.getOrDefault(emptyList())
        }
    }

    fun setActivity(name: String?) {
        _selectedActivity.value = if (_selectedActivity.value == name) null else name
    }

    fun filtered(): List<Homestay> {
        val a = _selectedActivity.value ?: return _all.value
        return _all.value.filter { it.activities.contains(a) }
    }

    fun seed(onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = runCatching { seeder.seedAll() }.isSuccess
            onDone(ok)
            if (ok) load()
        }
    }
}
