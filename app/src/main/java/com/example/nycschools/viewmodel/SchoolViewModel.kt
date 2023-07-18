package com.example.nycschools.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nycschools.model.data.SchoolDetailData
import com.example.nycschools.model.interfaces.SchoolDao
import com.example.nycschools.model.repo.FilterRepo
import com.example.nycschools.model.repo.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolViewModel @Inject constructor(
    private val schoolDao: SchoolDao,
    private val filterRepo: FilterRepo
): ViewModel() {

    val searchQuery = MutableStateFlow("")
    private val preferenceFlow = filterRepo.prefFlow

    /**
     * Combine two flows (1. searchQuery, 2. preferenceFlow) into one flow.
     * And call DAO operation when flow is changed.
     * Here, Flow can be changed when user update search text or sort preference.
     * */
    private val schoolDataFlow = combine(
        searchQuery,
        preferenceFlow
    ) { query, filterPreference ->
        Pair(query, filterPreference)
    }.flatMapLatest { (query,filterPreferences) ->
        schoolDao.getSchoolDetailData(query, filterPreferences.sortOrder)
    }

    /**
     * Convert the resultant flow into LiveData and observe it from Fragment.
     * We can also observe and "Collect" Flow in Fragment without converting it into LiveData.
     */
    val schools = schoolDataFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        filterRepo.updateSortOrder(sortOrder)
    }
}