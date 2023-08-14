package com.example.nycschools

import android.app.Application
import com.example.nycschools.model.data.SchoolDB
import com.example.nycschools.model.interfaces.SchoolDao
import com.example.nycschools.model.repo.FilterRepo
import com.example.nycschools.model.repo.NycRepository
import com.example.nycschools.webservice.NycApi
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NycSchoolsApplication: Application() {

}