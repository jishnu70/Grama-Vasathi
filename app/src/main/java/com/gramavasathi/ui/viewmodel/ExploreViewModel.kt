package com.gramavasathi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.launch

class ExploreViewModel : ViewModel() {
    private val repository = HomestayRepository()
    
    private val _homestays = MutableLiveData<List<Homestay>>()
    val homestays: LiveData<List<Homestay>> = _homestays

    init {
        loadAll()
    }

    private fun loadAll() {
        viewModelScope.launch {
            _homestays.value = repository.getAllHomestays()
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launch {
            val all = repository.getAllHomestays()
            _homestays.value = all.filter { 
                it.name.contains(query, ignoreCase = true) || 
                it.village.contains(query, ignoreCase = true) ||
                it.district.contains(query, ignoreCase = true)
            }
        }
    }

    fun setFilters(activities: Set<String>, minPrice: Float, maxPrice: Float) {
        viewModelScope.launch {
            val all = repository.getAllHomestays()
            _homestays.value = all.filter {
                (activities.isEmpty() || it.activities.containsAll(activities)) &&
                it.price_per_night >= minPrice && it.price_per_night <= maxPrice
            }
        }
    }
}
