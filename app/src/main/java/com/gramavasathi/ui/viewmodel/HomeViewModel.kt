package com.gramavasathi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeState(
    val homestays: List<Homestay> = emptyList(),
    val featured: List<Homestay> = emptyList(),
    val selectedActivities: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val wishlist: Set<String> = emptySet()
)

class HomeViewModel : ViewModel() {
    private val repository = HomestayRepository()
    
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val all = repository.getAllHomestays()
            _state.update { 
                it.copy(
                    homestays = all,
                    featured = all.filter { h -> h.rating >= 4.8 }.take(5),
                    isLoading = false
                )
            }
        }
    }

    fun toggleActivity(activity: String) {
        val current = _state.value.selectedActivities
        val next = if (current.contains(activity)) current - activity else current + activity
        _state.update { it.copy(selectedActivities = next) }
        filterHomestays(next)
    }

    private fun filterHomestays(activities: Set<String>) {
        viewModelScope.launch {
            val all = repository.getAllHomestays()
            val filtered = if (activities.isEmpty()) all else {
                all.filter { it.activities.containsAll(activities) }
            }
            _state.update { it.copy(homestays = filtered) }
        }
    }

    fun toggleWishlist(id: String) {
        val current = _state.value.wishlist
        val next = if (current.contains(id)) current - id else current + id
        _state.update { it.copy(wishlist = next) }
    }
}
