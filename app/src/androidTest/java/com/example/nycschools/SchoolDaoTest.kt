package com.example.nycschools

import android.util.Log
import androidx.test.filters.SmallTest
import com.example.nycschools.model.data.SATScore
import com.example.nycschools.model.data.School
import com.example.nycschools.model.data.SchoolDB
import com.example.nycschools.model.interfaces.SchoolDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class SchoolDaoTest {

    @get:Rule
    val rule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: SchoolDB
    lateinit var schoolDao: SchoolDao

    @Before
    fun setup() {
        rule.inject()
        schoolDao = database.SchoolDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertSchool() = runBlocking {
        val school = School(
            "Ideal School ",
            "1000",
            "83",
            "123456789",
            "867-450-242",
            "idealschool@gmail.com",
            "www.idealschool.com",
            "123 Example Avenue",
            "City",
            "10001",
            "State",
            "isc123"
        )
        schoolDao.insertSchool(listOf(school) )
        val record: Int = schoolDao.getRecord(school.id,School.tableName)
        Log.d("School", "Record found = $record")
        assert(record>0)
    }

    @Test
    fun testInsertSATScore() = runBlocking {
        val satScore = SATScore("100","323","432","231","isc123")
        schoolDao.insertSATScore(listOf(satScore))
        val record: Int = schoolDao.getRecord(satScore.id,SATScore.tableName)
        Log.d("SAT", "Record found = $record")
        assert(record>0)
    }
}