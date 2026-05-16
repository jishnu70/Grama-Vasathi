package com.gramavasathi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DetailState(
    val homestay: Homestay? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DetailViewModel : ViewModel() {
    private val repository = HomestayRepository()
    
    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    fun fetchHomestay(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val data = repository.getHomestayById(id)
            if (data != null) {
                _state.update { it.copy(homestay = data, isLoading = false) }
            } else {
                _state.update { it.copy(isLoading = false, error = "Homestay not found") }
            }
        }
    }
}
