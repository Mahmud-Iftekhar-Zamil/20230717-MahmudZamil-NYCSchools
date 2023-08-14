package com.example.nycschools.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.nycschools.model.repo.NycRepository
import com.example.nycschools.model.repo.SortOrder
import com.example.nycschools.utils.Events
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolViewModel @Inject constructor(
    private val nycRepository: NycRepository
): ViewModel() {

    private val svmEventChannel = Channel<Events>()
    val svmEvent = svmEventChannel.receiveAsFlow()
    val schools = nycRepository.schoolDataFlow.asLiveData()

    init {
        viewModelScope.launch {
            nycRepository.repoEvent.collect {event ->
                when(event) {
                    is Events.Success -> svmEventChannel.send(Events.Success)
                    is Events.Error -> svmEventChannel.send(Events.Error(event.error))
                }
            }
        }
        fetchData()
    }
    fun onSearchQueryChanged(searchQuery: String)  {
        nycRepository.searchQuery.value = searchQuery
    }
    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        nycRepository.onSortOrderChanged(sortOrder)
    }
    fun fetchData() = viewModelScope.launch {
        nycRepository.fetchDataFromServer()
    }
}