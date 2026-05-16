package com.gramavasathi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ExploreState(
    val results: List<Homestay> = emptyList(),
    val query: String = "",
    val selectedActivities: Set<String> = emptySet(),
    val priceRange: ClosedFloatingPointRange<Float> = 0f..5000f,
    val isLoading: Boolean = false
)

@OptIn(FlowPreview::class)
class ExploreViewModel : ViewModel() {
    private val repository = HomestayRepository()
    
    private val _state = MutableStateFlow(ExploreState())
    val state: StateFlow<ExploreState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        loadInitial()
        
        // Debounced search
        _searchQuery
            .debounce(500)
            .distinctUntilChanged()
            .onEach { query ->
                _state.update { it.copy(query = query) }
                applyFilters()
            }
            .launchIn(viewModelScope)
    }

    private fun loadInitial() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val all = repository.getAllHomestays()
            _state.update { it.copy(results = all, isLoading = false) }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun updateFilters(activities: Set<String>, range: ClosedFloatingPointRange<Float>) {
        _state.update { it.copy(selectedActivities = activities, priceRange = range) }
        applyFilters()
    }

    private fun applyFilters() {
        viewModelScope.launch {
            val all = repository.getAllHomestays()
            val query = _state.value.query
            val activities = _state.value.selectedActivities
            val range = _state.value.priceRange
            
            val filtered = all.filter { h ->
                val matchesQuery = h.name.contains(query, true) || 
                                 h.village.contains(query, true) || 
                                 h.district.contains(query, true)
                val matchesActivities = activities.isEmpty() || h.activities.any { it in activities }
                val matchesPrice = h.price_per_night >= range.start && h.price_per_night <= range.endInclusive
                
                matchesQuery && matchesActivities && matchesPrice
            }
            _state.update { it.copy(results = filtered) }
        }
    }
}
