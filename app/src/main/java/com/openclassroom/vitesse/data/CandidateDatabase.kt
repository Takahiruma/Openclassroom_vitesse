package com.openclassroom.vitesse.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassroom.vitesse.data.CandidateEntity
import com.openclassroom.vitesse.data.CandidateDao

@Database(entities = [CandidateEntity::class], version = 1, exportSchema = false)
abstract class CandidateDatabase : RoomDatabase() {

    abstract fun candidateDao(): CandidateDao

    companion object {
        @Volatile
        private var INSTANCE: CandidateDatabase? = null

        fun getDatabase(context: Context): CandidateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CandidateDatabase::class.java,
                    "candidate_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}