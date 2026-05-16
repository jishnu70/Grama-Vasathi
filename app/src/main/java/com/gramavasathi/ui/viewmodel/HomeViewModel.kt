package com.gramavasathi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = HomestayRepository()
    
    private val _homestays = MutableLiveData<List<Homestay>>()
    val homestays: LiveData<List<Homestay>> = _homestays
    
    private val _featuredHomestays = MutableLiveData<List<Homestay>>()
    val featuredHomestays: LiveData<List<Homestay>> = _featuredHomestays
    
    private val _selectedActivities = MutableLiveData<Set<String>>(emptySet())
    val selectedActivities: LiveData<Set<String>> = _selectedActivities

    init {
        loadHomestays()
    }

    private fun loadHomestays() {
        viewModelScope.launch {
            val list = repository.getAllHomestays()
            _homestays.value = list
            _featuredHomestays.value = list.filter { it.rating >= 4.8 }.take(3)
        }
    }

    fun toggleActivity(activity: String) {
        val current = _selectedActivities.value ?: emptySet()
        val newSet = if (current.contains(activity)) current - activity else current + activity
        _selectedActivities.value = newSet
        applyFilters(newSet)
    }

    private fun applyFilters(activities: Set<String>) {
        viewModelScope.launch {
            val all = repository.getAllHomestays()
            _homestays.value = if (activities.isEmpty()) all else {
                all.filter { it.activities.containsAll(activities) }
            }
        }
    }
}
