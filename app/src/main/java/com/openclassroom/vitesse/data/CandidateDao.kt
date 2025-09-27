package com.openclassroom.vitesse.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Query("SELECT * FROM candidate")
    fun getAllCandidates(): Flow<List<CandidateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidate(candidate: CandidateEntity)

    @Delete
    suspend fun deleteCandidate(candidate: CandidateEntity)
}