package com.gramavasathi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramavasathi.data.model.Homestay
import com.gramavasathi.data.repository.HomestayRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val repository = HomestayRepository()
    private val _homestay = MutableLiveData<Homestay?>()
    val homestay: LiveData<Homestay?> = _homestay

    fun fetchHomestay(id: String) {
        viewModelScope.launch {
            _homestay.value = repository.getHomestayById(id)
        }
    }
}
