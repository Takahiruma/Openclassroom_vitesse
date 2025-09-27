package com.openclassroom.vitesse.repository

import com.openclassroom.vitesse.data.Candidate
import com.openclassroom.vitesse.data.CandidateDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.openclassroom.vitesse.data.toDomain
import com.openclassroom.vitesse.data.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CandidateRepository(
    private val candidateDao: CandidateDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    val candidates: Flow<List<Candidate>> = candidateDao.getAllCandidates()
        .map { list -> list.map { it.toDomain() } }

    suspend fun addCandidate(candidate: Candidate) {
        withContext(ioDispatcher) {
            candidateDao.insertCandidate(candidate.toEntity())
        }
    }

    suspend fun removeCandidate(candidate: Candidate) {
        withContext(ioDispatcher)
        { candidateDao.deleteCandidate(candidate.toEntity()) }
    }

    suspend fun updateCandidate(candidate: Candidate) {
        withContext(ioDispatcher)
        { candidateDao.insertCandidate(candidate.toEntity()) }
    }

    suspend fun toggleFavorite(candidate: Candidate) {
        withContext(ioDispatcher) {
            val updatedCandidate = candidate.copy(isFavorite = !candidate.isFavorite)
            updateCandidate(updatedCandidate)
        }
    }
}
