package com.example.nycschools.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_SATScore")
data class SATScore (
    val testTakers: Int,
    val criticalReading: Int,
    val math: Int,
    val writing: Int,
    @PrimaryKey val id: String
)