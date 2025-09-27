package com.openclassroom.vitesse.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassroom.vitesse.repository.ExchangeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExchangeViewModel(
    private val repository: ExchangeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    val conversionRate = MutableStateFlow(0.0)

    fun loadConversionRate(fromCurrency: String, toCurrency: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val ratesResponse = repository.fetchRates(fromCurrency.lowercase())
                val rate = ratesResponse.rates[toCurrency.lowercase()] ?: 0.0
                conversionRate.value = rate
            } catch (e: Exception) {
                conversionRate.value = 0.0
            }
        }
    }
}