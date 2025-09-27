package com.openclassroom.vitesse.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Calendar

@Entity(tableName = "candidate")
@TypeConverters(CalendarConverter::class)
data class CandidateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val photo: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: Calendar,
    val phoneNumber: String,
    val email: String,
    val note: String,
    val salary: Double,
    val isFavorite: Boolean = false
)