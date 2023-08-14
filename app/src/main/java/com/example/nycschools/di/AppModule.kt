package com.example.nycschools.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.nycschools.model.data.SchoolDB
import com.example.nycschools.utils.NetworkConnectivityStatus
import com.example.nycschools.webservice.NycApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(NycApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideNycApi(retrofit: Retrofit): NycApi =
        retrofit.create(NycApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
    ) = Room.databaseBuilder(app, SchoolDB::class.java,"db_school")
        .fallbackToDestructiveMigration()
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
