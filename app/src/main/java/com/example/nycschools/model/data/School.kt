package com.example.nycschools.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = School.tableName)

data class School(
    val name: String?,
    val totalStudents: String?,
    val attendanceRate: String?,
    val phone: String?,
    val fax: String?,
    val email: String?,
    val website: String?,
    val address: String?,
    val city: String?,
    val zip: String?,
    val state: String?,
    @PrimaryKey val id: String
) {
    companion object {
        const val tableName = "tbl_School"
    }
}
