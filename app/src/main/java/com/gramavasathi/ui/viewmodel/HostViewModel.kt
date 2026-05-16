package com.gramavasathi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gramavasathi.data.model.ChecklistItem

class HostViewModel : ViewModel() {
    private val allItems = listOf(
        ChecklistItem("clean_sheets", "Hygiene", "Fresh Clean Sheets", "Freshly washed and sun-dried linens for every new guest.", 12),
        ChecklistItem("safe_water", "Safety", "Safe Drinking Water", "Filtered or boiled water available at all times.", 15),
        ChecklistItem("western_toilet", "Hygiene", "Western-style Toilet", "Available and clean for guest convenience.", 10),
        ChecklistItem("clean_bathroom", "Hygiene", "Clean Bathroom", "Stocked with soap and clean towels.", 8),
        ChecklistItem("mosquito_net", "Safety", "Mosquito Nets", "Installed on all beds to ensure safe sleep.", 8),
        ChecklistItem("home_cooked", "Food", "Home-cooked Meals", "Hygienic and authentic local village food.", 12),
        ChecklistItem("food_allergies", "Food", "Ask About Allergies", "Confirm dietary restrictions before cooking.", 5),
        ChecklistItem("welcome_note", "Experience", "Written Welcome Note", "A handwritten greeting in the guest room.", 5),
        ChecklistItem("local_guide", "Experience", "Printed Local Guide", "Information about nearby spots and village norms.", 5),
        ChecklistItem("activity_schedule", "Experience", "Activity Schedule", "Clear plan for farm activities and timings.", 10),
        ChecklistItem("first_aid", "Safety", "First Aid Kit", "Basic medical supplies accessible to guests.", 5),
        ChecklistItem("phone_charged", "Comfort", "Charging Point", "Dedicated phone charging point in the room.", 5)
    )

    val categories = listOf("Hygiene", "Safety", "Food", "Experience", "Comfort")

    private val _currentStep = MutableLiveData(0)
    val currentStep: LiveData<Int> = _currentStep

    private val _checkedItems = MutableLiveData<Set<String>>(emptySet())
    val checkedItems: LiveData<Set<String>> = _checkedItems

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    fun getItemsForCategory(category: String): List<ChecklistItem> {
        return allItems.filter { it.category == category }
    }

    fun toggleItem(id: String) {
        val current = _checkedItems.value ?: emptySet()
        val newSet = if (current.contains(id)) current - id else current + id
        _checkedItems.value = newSet
        calculateScore(newSet)
    }

    private fun calculateScore(checked: Set<String>) {
        val total = allItems.filter { checked.contains(it.id) }.sumOf { it.points }
        _score.value = total
    }

    fun nextStep() {
        val current = _currentStep.value ?: 0
        if (current < categories.size) {
            _currentStep.value = current + 1
        }
    }

    fun prevStep() {
        val current = _currentStep.value ?: 0
        if (current > 0) {
            _currentStep.value = current - 1
        }
    }
    
    fun getCategoryScore(category: String): Pair<Int, Int> {
        val categoryItems = allItems.filter { it.category == category }
        val maxPoints = categoryItems.sumOf { it.points }
        val earnedPoints = categoryItems.filter { _checkedItems.value?.contains(it.id) == true }.sumOf { it.points }
        return earnedPoints to maxPoints
    }
}
