package com.example.nycschools.model.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.nycschools.model.data.SATScore
import com.example.nycschools.model.data.School
import com.example.nycschools.model.data.SchoolDetailData
import com.example.nycschools.model.repo.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchool(school: School)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSATScore(satScore: SATScore)

    @RawQuery(observedEntities = [School::class,SATScore::class])
    fun query(query:SupportSQLiteQuery): Int

    fun getRecord(schoolId: String, tableName: String): Int {
        val queryText = SimpleSQLiteQuery("SELECT COUNT(*) AS CNT FROM $tableName WHERE id = \'$schoolId\'")
        return this.query(queryText)
    }

    @Query("""
        SELECT tbl_School.id, tbl_School.name, tbl_School.totalStudents, tbl_School.attendanceRate, tbl_School.phone,
                tbl_School.fax, tbl_School.email, tbl_School.website, tbl_School.address, tbl_School.city, 
                tbl_School.zip, tbl_School.state, tbl_SATScore.testTakers, tbl_SATScore.criticalReading, 
                tbl_SATScore.math, tbl_SATScore.writing 
                FROM tbl_School 
                JOIN tbl_SATScore ON tbl_School.id = tbl_SATScore.id 
                WHERE tbl_SATScore.testTakers > 0 AND tbl_School.name LIKE '%' || :schoolName || '%' 
                ORDER BY (tbl_SATScore.criticalReading + tbl_SATScore.math + tbl_SATScore.writing) DESC , tbl_School.name
    """)
    fun getSchoolDetailDataDESC(schoolName: String): Flow<List<SchoolDetailData>>

    @Query("""
        SELECT tbl_School.id, tbl_School.name, tbl_School.totalStudents, tbl_School.attendanceRate, tbl_School.phone,
                tbl_School.fax, tbl_School.email, tbl_School.website, tbl_School.address, tbl_School.city, 
                tbl_School.zip, tbl_School.state, tbl_SATScore.testTakers, tbl_SATScore.criticalReading, 
                tbl_SATScore.math, tbl_SATScore.writing 
                FROM tbl_School 
                JOIN tbl_SATScore ON tbl_School.id = tbl_SATScore.id 
                WHERE tbl_SATScore.testTakers > 0 AND tbl_School.name LIKE '%' || :schoolName || '%' 
                ORDER BY (tbl_SATScore.criticalReading + tbl_SATScore.math + tbl_SATScore.writing) ASC , tbl_School.name
    """)
    fun getSchoolDetailDataASC(schoolName: String): Flow<List<SchoolDetailData>>

    fun getSchoolDetailData(schoolName: String, sortOrder: SortOrder): Flow<List<SchoolDetailData>> =
        when(sortOrder){
            SortOrder.BY_ASC -> getSchoolDetailDataASC(schoolName)
            SortOrder.BY_DESC -> getSchoolDetailDataDESC(schoolName)
        }
}