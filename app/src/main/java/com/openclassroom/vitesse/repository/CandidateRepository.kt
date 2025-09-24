package com.openclassroom.vitesse.repository

import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.data.CandidateData
import com.openclassroom.vitesse.service.CandidateApiService
import com.openclassroom.vitesse.service.LocalCandidateApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class CandidateRepository {

    /**
     * Le service API pour interagir avec les candidats.
     */
    private val candidateApiService: CandidateApiService = LocalCandidateApiService()

    /**
     * Flow émettant la liste des candidats.
     */
    private val _candidates = MutableStateFlow<List<Candidate>>(emptyList())
    open val candidates: StateFlow<List<Candidate>> = _candidates.asStateFlow()

    init {
        candidateApiService.init()
        refreshCandidates()
    }

    private fun refreshCandidates() {
        _candidates.value = candidateApiService.getAllCandidates().toList()
    }

    open fun addCandidate(candidate: Candidate) {
        candidateApiService.addCandidate(candidate)
        refreshCandidates()
    }

    open fun removeCandidate(candidate: Candidate) {
        candidateApiService.removeCandidate(candidate)
        refreshCandidates()
    }

    open fun updateCandidate(candidate: Candidate) {
        candidateApiService.updateCandidate(candidate)
        refreshCandidates()
    }

    open fun toggleFavorite(candidate: Candidate) {
        val updatedList = _candidates.value.map {
            if (it.id == candidate.id) {
                val updatedCandidate = it.copy(isFavorite = !it.isFavorite)
                candidateApiService.updateCandidate(updatedCandidate)
                updatedCandidate
            } else {
                it
            }
        }
        _candidates.value = updatedList
        refreshCandidates()
    }

}
