package com.example.nycschools.webservice

import com.example.nycschools.model.data.SATScore
import com.example.nycschools.model.data.School
import retrofit2.Response
import retrofit2.http.GET

interface NycApi {
    companion object {
        const val BASE_URL = "https://data.cityofnewyork.us/resource/"
    }

    @GET("s3k6-pzi2.json")
    suspend fun getSchoolDataFromWeb(): Response<List<School>>

    @GET("f9bf-2cp4.json")
    suspend fun getSchoolSATDataFromWeb(): Response<List<SATScore>>
}