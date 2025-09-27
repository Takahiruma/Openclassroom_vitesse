package com.openclassroom.vitesse.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.repository.CandidateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CandidateViewModel(
    private val repository: CandidateRepository
) : ViewModel() {

    private val _candidates = MutableStateFlow<List<Candidate>>(emptyList())
    val candidates: StateFlow<List<Candidate>> = _candidates

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _isLoading.value = true
            repository.candidates.collect { list ->
                _candidates.value = list
                _isLoading.value = false
            }
        }
    }

    fun addCandidate(candidate: Candidate) {
        viewModelScope.launch {
            repository.addCandidate(candidate)
        }
    }

    fun removeCandidate(candidate: Candidate) {
        viewModelScope.launch {
            repository.removeCandidate(candidate)
        }
    }

    fun updateCandidate(candidate: Candidate) {
        viewModelScope.launch {
            repository.updateCandidate(candidate)
        }
    }

    fun addOrUpdateCandidate(candidate: Candidate) {
        val exists = _candidates.value.any { it.id == candidate.id }
        if (exists) {
            updateCandidate(candidate)
        } else {
            addCandidate(candidate)
        }
    }

    fun toggleFavorite(candidate: Candidate) {
        viewModelScope.launch {
            repository.toggleFavorite(candidate)
        }
    }
}

