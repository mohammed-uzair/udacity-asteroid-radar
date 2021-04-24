package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.data_source.database.dao.AsteroidDao
import com.udacity.asteroidradar.data_source.web.Api
import com.udacity.asteroidradar.data_source.web.utils.parseAsteroidsJsonResult
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.util.DateTimeUtil
import com.udacity.asteroidradar.view_models.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response

class AsteroidRepository(private val asteroidDao: AsteroidDao?) {
    fun getAllNearEarthAsteroids(): Flow<List<Asteroid>> = callbackFlow {
        // Get all the near earth asteroids from web source
        val startDate = DateTimeUtil.getCurrentDate()
        val endDate = DateTimeUtil.getDateAfter()

        Api.AsteroidEndpoints.getAllNearingAsteroids(startDate, endDate)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    // Raw string response
                    val stringResponse = response.body()?.string()
                    val allAsteroids = parseAsteroidsJsonResult(JSONObject(stringResponse ?: ""))
                    offer(allAsteroids)

                    CoroutineScope(Dispatchers.IO).launch {
                        // Save a cached version of all the asteroids in the local DB
                        asteroidDao?.saveNearEarthAsteroids(allAsteroids)
                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    Log.e(MainViewModel.TAG, t.message, t)
                }
            })
    }

    suspend fun getImageOfTheDay(): PictureOfDay = Api.AsteroidEndpoints.getImageOfTheDay()
}