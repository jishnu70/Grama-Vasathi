package com.gramavasathi.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {
    private val repo = HomestayRepository()

    private val _all = MutableStateFlow<List<Homestay>>(emptyList())
    val all: StateFlow<List<Homestay>> = _all.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _selectedActivities = MutableStateFlow(setOf<String>())
    val selectedActivities = _selectedActivities.asStateFlow()

    val activityList = listOf("Cow Milking", "Field Plowing", "Local Cooking", "Bird Watching", "Sunrise Trek", "Fishing", "Herb Garden", "Nature Walk")

    private val _priceMin = MutableStateFlow(500f)
    private val _priceMax = MutableStateFlow(3000f)
    val priceMin = _priceMin.asStateFlow()
    val priceMax = _priceMax.asStateFlow()

    private val _minScore = MutableStateFlow(0f)
    val minScore = _minScore.asStateFlow()

    private val _sort = MutableStateFlow("Rating")
    val sort = _sort.asStateFlow()

    private var searchJob: Job? = null

    fun load() {
        viewModelScope.launch {
            _all.value = runCatching { repo.getAllHomestays() }.getOrDefault(emptyList())
        }
    }

    fun onQueryChange(v: String) {
        _query.value = v
        searchJob?.cancel()
        searchJob = viewModelScope.launch { kotlinx.coroutines.delay(400) }
    }

    fun toggleActivity(a: String) {
        _selectedActivities.value = _selectedActivities.value.toMutableSet().apply {
            if (contains(a)) remove(a) else add(a)
        }
    }

    fun setPrice(min: Float, max: Float) {
        _priceMin.value = min
        _priceMax.value = max
    }

    fun setMinScore(v: Float) { _minScore.value = v }
    fun setSort(v: String) { _sort.value = v }
    fun clearAll() {
        _query.value = ""
        _selectedActivities.value = emptySet()
        _priceMin.value = 500f
        _priceMax.value = 3000f
        _minScore.value = 0f
    }

    fun filtered(): List<Homestay> {
        var out = _all.value.filter {
            (it.name.contains(_query.value, true) || it.village.contains(_query.value, true) || it.district.contains(_query.value, true) || it.activities.any { a -> a.contains(_query.value, true) }) &&
                    it.price_per_night in _priceMin.value.toInt().._priceMax.value.toInt() &&
                    it.host_readiness_score >= _minScore.value.toInt() &&
                    // Intentional ALL-match logic: homestay must contain every selected activity.
                    _selectedActivities.value.all { act -> it.activities.contains(act) }
        }
        out = when (_sort.value) {
            "Price: Low to High" -> out.sortedBy { it.price_per_night }
            "Price: High to Low" -> out.sortedByDescending { it.price_per_night }
            "Host Score" -> out.sortedByDescending { it.host_readiness_score }
            else -> out.sortedByDescending { it.rating }
        }
        return out
    }
}
