package com.example.nycschools.model.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nycschools.R
import com.example.nycschools.di.ApplicationScope
import com.example.nycschools.di.CoroutineIOScope
import com.example.nycschools.model.interfaces.SchoolDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Provider

private const val TAG: String = "SchoolDB"

@Database(entities = [School::class, SATScore::class], version = 5)
abstract class SchoolDB : RoomDatabase() {
    abstract fun SchoolDao(): SchoolDao

    class Callback @Inject constructor(
        private val database: Provider<SchoolDB>,
        @CoroutineIOScope private val coroutineIOScope: CoroutineScope,
        @ApplicationContext private val context: Context
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().SchoolDao()
            coroutineIOScope.launch {
                loadSchool(context, dao)
                loadSatScore(context, dao)
            }
        }

        /**
         * Load data locally from JSON.
         * We can also load the data directly from URLs using Retrofit.
         * But provided data links are static in nature. So do not use GET / REST calls.
         * As an improvement we can use Retrofit later.
         * */

        private suspend fun loadSchool(context: Context, dao: SchoolDao) {
            try{
                val schoolList: JSONArray = context.resources.openRawResource(R.raw.nycschooldata).bufferedReader().use {
                    JSONArray(it.readText())
                }

                schoolList.takeIf { it.length() > 0 }?.let { list ->
                    Log.i(TAG, "School count: ${schoolList.length()}")
                    for(idx in 0 until list.length()){
                        val schoolObj = list.getJSONObject(idx)
                        dao.insertSchool(
                            School(
                                schoolObj.optString("school_name",""),
                                schoolObj.optString("total_students",""),
                                schoolObj.optString("attendance_rate",""),
                                schoolObj.optString("phone_number",""),
                                schoolObj.optString("fax_number",""),
                                schoolObj.optString("school_email",""),
                                schoolObj.optString("website",""),
                                schoolObj.optString("primary_address_line_1",""),
                                schoolObj.optString("city",""),
                                schoolObj.optString("zip",""),
                                schoolObj.optString("state_code",""),
                                schoolObj.optString("dbn")
                            )
                        )
                    }
                }

            }catch (ex: Exception) {
                Log.e(TAG, ex.message.toString())
            }
        }

        private suspend fun loadSatScore(context: Context, dao: SchoolDao) {
            try{
                val SatScoreList: JSONArray = context.resources.openRawResource(R.raw.nycschoolsatdata).bufferedReader().use {
                    JSONArray(it.readText())
                }

                SatScoreList.takeIf { it.length() > 0 }?.let { list ->
                    for(idx in 0 until list.length()){
                        val satObj = list.getJSONObject(idx)
                        dao.insertSATScore(
                            SATScore(
                                satObj.optInt("num_of_sat_test_takers",0),
                                satObj.optInt("sat_critical_reading_avg_score",0),
                                satObj.optInt("sat_math_avg_score",0),
                                satObj.optInt("sat_writing_avg_score",0),
                                satObj.optString("dbn")
                            )
                        )
                    }
                }

            }catch (ex: Exception) {
                Log.e(TAG, ex.message.toString())
            }
        }
    }


}