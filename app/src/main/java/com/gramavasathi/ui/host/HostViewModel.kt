package com.gramavasathi.ui.host

import androidx.lifecycle.ViewModel
import com.gramavasathi.utils.checklistItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HostViewModel : ViewModel() {
    val categories = listOf("Hygiene", "Safety", "Food", "Experience", "Comfort", "Score")
    private val _step = MutableStateFlow(0)
    val step: StateFlow<Int> = _step.asStateFlow()

    private val _checked = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val checked = _checked.asStateFlow()

    fun toggle(id: String) {
        _checked.value = _checked.value.toMutableMap().apply { put(id, !(this[id] ?: false)) }
    }

    fun next() { _step.value = (_step.value + 1).coerceAtMost(categories.lastIndex) }
    fun back() { _step.value = (_step.value - 1).coerceAtLeast(0) }

    fun score(): Int = checklistItems.filter { _checked.value[it.id] == true }.sumOf { it.points }

    fun currentItems() = if (categories[_step.value] == "Score") emptyList() else checklistItems.filter { it.category == categories[_step.value] }

    fun categoryBreakdown(): List<Pair<String, Pair<Int, Int>>> {
        return categories.filter { it != "Score" }.map { c ->
            val items = checklistItems.filter { it.category == c }
            val earned = items.filter { _checked.value[it.id] == true }.sumOf { it.points }
            c to (earned to items.sumOf { it.points })
        }
    }
}
