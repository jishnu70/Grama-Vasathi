package com.gramavasathi.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val repo = HomestayRepository()
    private val _homestay = MutableStateFlow<Homestay?>(null)
    val homestay: StateFlow<Homestay?> = _homestay.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch { _homestay.value = repo.getHomestayById(id) }
    }
}
