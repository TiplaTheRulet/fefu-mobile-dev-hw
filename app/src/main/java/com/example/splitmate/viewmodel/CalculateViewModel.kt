package com.example.splitmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitmate.data.Calculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculationViewModel : ViewModel() {
    private val _currentCalculation = MutableStateFlow(Calculation())
    val currentCalculation: StateFlow<Calculation> = _currentCalculation.asStateFlow()

    private val _calculations = mutableListOf<Calculation>()

    fun updateTotalAmount(amount: String) {
        viewModelScope.launch {
            val newAmount = amount.toDoubleOrNull() ?: 0.0
            val current = _currentCalculation.value
            _currentCalculation.value = current.copy(totalAmount = newAmount)
        }
    }

    fun updatePeopleCount(count: String) {
        viewModelScope.launch {
            val newCount = count.toIntOrNull() ?: 1
            val current = _currentCalculation.value
            _currentCalculation.value = current.copy(peopleCount = newCount)
        }
    }

    fun updateTipPercentage(percentage: Double) {
        viewModelScope.launch {
            val current = _currentCalculation.value
            _currentCalculation.value = current.copy(tipPercentage = percentage)
        }
    }

    fun saveCalculation() {
        viewModelScope.launch {
            val calculation = _currentCalculation.value.copy()
            _calculations.add(calculation)
            _currentCalculation.value = Calculation()
        }
    }

    fun resetCalculation() {
        viewModelScope.launch {
            _currentCalculation.value = Calculation()
        }
    }

    fun getCalculationById(id: String): Calculation? {
        return _calculations.find { it.id == id }
    }
}