package com.example.nycschools.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SchoolDetailData(
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
    val testTakers: Int? = 0,
    val criticalReading: Int? = 0,
    val math: Int? = 0,
    val writing: Int? = 0,
    val id: String
): Parcelable
