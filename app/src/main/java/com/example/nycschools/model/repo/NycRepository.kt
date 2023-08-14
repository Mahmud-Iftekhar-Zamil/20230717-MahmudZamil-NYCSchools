package com.example.nycschools.model.repo

import android.util.Log
import com.example.nycschools.model.interfaces.SchoolDao
import com.example.nycschools.utils.Events
import com.example.nycschools.utils.NetworkConnectivityStatus
import com.example.nycschools.webservice.NycApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NycRepository @Inject constructor(
    private val schoolDao: SchoolDao,
    private val filterRepo: FilterRepo,
    private val nycApi: NycApi
) {
    private val preferenceFlow = filterRepo.prefFlow
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery

    private val repoEventChannel = Channel<Events>()
    val repoEvent = repoEventChannel.receiveAsFlow()

    /**
     * Combine two flows (1. searchQuery, 2. preferenceFlow) into one flow.
     * And call DAO operation when flow is changed.
     * Here, Flow can be changed when user update search text or sort preference.
     * */
    private val updatedFlow = combine(
        searchQuery,
        preferenceFlow
    ) { query, filterPreference ->
        Pair(query, filterPreference)
    }.flatMapLatest { (query,filterPreferences) ->
        schoolDao.getSchoolDetailData(query, filterPreferences.sortOrder)
    }

    val schoolDataFlow = updatedFlow

    suspend fun onSortOrderChanged(sortOrder: SortOrder) = filterRepo.updateSortOrder(sortOrder)

    suspend fun fetchDataFromServer() {
        try {
            val resultSchoolData = nycApi.getSchoolDataFromWeb()
            resultSchoolData.body()?.let {schoolDao.insertSchool(it) }

            val resultSATData = nycApi.getSchoolSATDataFromWeb()
            resultSATData.body()?.let { schoolDao.insertSATScore(it) }

            repoEventChannel.send(Events.Success)
        }catch (e: Exception) {
            Log.e("TEST" , "${e.message.toString()}")
            repoEventChannel.send(Events.Error(e))
        }
    }
}