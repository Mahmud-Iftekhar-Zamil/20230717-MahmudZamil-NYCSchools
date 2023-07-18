package com.example.nycschools.di

import android.app.Application
import androidx.room.Room
import com.example.nycschools.model.data.SchoolDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: SchoolDB.Callback
    ) = Room.databaseBuilder(app, SchoolDB::class.java,"db_school")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideSchoolDao(db: SchoolDB) = db.SchoolDao()

    /**
     * Create multiple type of coroutine scopes (default, IO, Main).
     * And dependency injection when needed
     * */

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @CoroutineIOScope
    @Provides
    @Singleton
    fun provideCoroutineIOScope() = CoroutineScope(Dispatchers.IO)
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CoroutineIOScope