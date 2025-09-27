package com.openclassroom.vitesse.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassroom.vitesse.repository.ExchangeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ExchangeViewModelFactory(
    private val repository: ExchangeRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExchangeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExchangeViewModel(repository, dispatcher) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}