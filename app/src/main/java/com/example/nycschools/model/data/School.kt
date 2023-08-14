package com.example.nycschools.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = School.tableName)

data class School(
    @SerializedName("school_name")
    @Expose
    val name: String? = "",
    @SerializedName("total_students")
    @Expose
    val totalStudents: String? = "0",
    @SerializedName("attendance_rate")
    @Expose
    val attendanceRate: String? = "",
    @SerializedName("phone_number")
    @Expose
    val phone: String? = "",
    @SerializedName("fax_number")
    @Expose
    val fax: String? = "",
    @SerializedName("school_email")
    @Expose
    val email: String? = "",
    @SerializedName("website")
    @Expose
    val website: String? = "",
    @SerializedName("primary_address_line_1")
    @Expose
    val address: String? = "",
    @SerializedName("city")
    @Expose
    val city: String? = "",
    @SerializedName("zip")
    @Expose
    val zip: String? = "",
    @SerializedName("state_code")
    @Expose
    val state: String? = "",
    @SerializedName("dbn")
    @Expose
    @PrimaryKey val id: String
) {
    companion object {
        const val tableName = "tbl_School"
    }
}

