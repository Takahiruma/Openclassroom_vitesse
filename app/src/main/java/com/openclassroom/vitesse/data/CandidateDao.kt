package com.openclassroom.vitesse.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Query("SELECT * FROM candidate")
    fun getAllCandidates(): Flow<List<CandidateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCandidate(candidate: CandidateEntity)

    @Delete
    fun deleteCandidate(candidate: CandidateEntity)
}