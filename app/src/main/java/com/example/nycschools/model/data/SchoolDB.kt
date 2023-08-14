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
import com.example.nycschools.model.repo.NycRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Provider

private const val TAG: String = "SchoolDB"

@Database(entities = [School::class, SATScore::class], version = 10)
abstract class SchoolDB : RoomDatabase() {
    abstract fun SchoolDao(): SchoolDao
}