package com.openclassroom.vitesse.viewModel

import CandidateViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassroom.vitesse.repository.CandidateRepository

class CandidateViewModelFactory(
    private val repository: CandidateRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CandidateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CandidateViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}