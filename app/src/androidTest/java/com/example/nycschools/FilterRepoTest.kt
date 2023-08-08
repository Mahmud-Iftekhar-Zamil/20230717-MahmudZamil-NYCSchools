package com.example.nycschools

import com.example.nycschools.model.repo.FilterRepo
import com.example.nycschools.model.repo.SortOrder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class FilterRepoTest {

    @get:Rule
    val rule = HiltAndroidRule(this)

    @Inject
    lateinit var filterRepo: FilterRepo

    @Before
    fun setup() {
        rule.inject()
    }

    @Test
    fun testUpdateSortOrder() = runBlocking {
        val testSortOrder = SortOrder.BY_DESC

        filterRepo.updateSortOrder(testSortOrder)
        val actualSortOrder: String = filterRepo.getCurrentSortOrderFromDataStore()
        assert(actualSortOrder.contentEquals(testSortOrder.name))
    }
}