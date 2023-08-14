package com.example.nycschools.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = SATScore.tableName)
data class SATScore (
    @SerializedName("num_of_sat_test_takers")
    @Expose
    val testTakers: String? = "",
    @SerializedName("sat_critical_reading_avg_score")
    @Expose
    val criticalReading: String? = "",
    @SerializedName("sat_math_avg_score")
    @Expose
    val math: String? = "",
    @SerializedName("sat_writing_avg_score")
    @Expose
    val writing: String? = "",
    @SerializedName("dbn")
    @Expose
    @PrimaryKey val id: String
) {
    companion object {
        const val tableName = "tbl_SATScore"
    }
}
