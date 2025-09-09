package com.openclassroom.vitesse.service

import com.openclassroom.vitesse.data.Candidate

interface CandidateApiService {
    /**
     * Ajoute un candidat
     * @param candidate Le candidat à ajouter
     */
    fun addCandidate(candidate: Candidate)

    /**
     * Retourne tous les candidats
     * @return la liste des candidats
     */
    fun getAllCandidates(): List<Candidate>

    /**
     * Supprime un candidat
     * @param candidate Le candidat à supprimer
     */
    fun removeCandidate(candidate: Candidate)
}
