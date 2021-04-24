package com.udacity.asteroidradar.data_source.web.api

import com.udacity.asteroidradar.models.PictureOfDay
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidsApi {
    @GET("planetary/apod")
    suspend fun getImageOfTheDay(): PictureOfDay

    @GET("neo/rest/v1/feed")
    fun getAllNearingAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Call<ResponseBody>
}