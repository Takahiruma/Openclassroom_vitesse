package com.openclassroom.vitesse.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CandidateFakeDao : CandidateDao {
    private val candidatesList = mutableListOf<CandidateEntity>()
    private val flow = MutableStateFlow<List<CandidateEntity>>(emptyList())

    override fun getAllCandidates(): Flow<List<CandidateEntity>> = flow

    override suspend fun insertCandidate(candidate: CandidateEntity) {
        val index = candidatesList.indexOfFirst { it.id == candidate.id }
        if (index != -1) {
            candidatesList[index] = candidate
        } else {
            candidatesList.add(candidate)
        }
        flow.emit(candidatesList.toList())
    }

    override suspend fun deleteCandidate(candidate: CandidateEntity) {
        candidatesList.removeIf { it.id == candidate.id }
        flow.value = candidatesList.toList()
    }
}