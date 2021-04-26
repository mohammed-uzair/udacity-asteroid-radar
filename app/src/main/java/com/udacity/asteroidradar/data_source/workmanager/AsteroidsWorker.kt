package com.udacity.asteroidradar.data_source.workmanager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.data_source.database.AsteroidsDatabase.Companion.getAppDatabase
import com.udacity.asteroidradar.data_source.web.Api
import com.udacity.asteroidradar.data_source.web.utils.parseAsteroidsJsonResult
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.util.DateTimeUtil
import com.udacity.asteroidradar.util.DateTimeUtil.areAsteroidsOutDated
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AsteroidsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    companion object {
        const val TAG = "AsteroidsWorker"
        const val WORK_NAME = "FetchNewAsteroids"
    }

    override suspend fun doWork(): Result {
        return try {
            fetchPictureOfDay()
            fetchAsteroids()

            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    private suspend fun fetchAsteroids() {
        // Get Asteroids only if we do not have the latest Asteroids in our cached database
        getAppDatabase(applicationContext).asteroidDao.getAllAsteroids().collect { asteroids ->
            if (asteroids == null || asteroids.isEmpty() || areAsteroidsOutDated(asteroids[0].closeApproachDate)) {
                getAsteroids().collect { saveAsteroids(it) }
            }
        }
    }

    private suspend fun fetchPictureOfDay() {
        try {
            val pictureOfDay = Api.AsteroidEndpoints.getPictureOfDay()
            if (pictureOfDay != null) {
                // Cache the picture of the day
                savePictureOfDay(pictureOfDay)
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.message, exception)
        }
    }

    private fun savePictureOfDay(pictureOfDay: PictureOfDay) {
        getAppDatabase(applicationContext).pictureOfDayDao.savePictureOfDay(pictureOfDay)
    }

    private fun getAsteroids(): Flow<List<Asteroid>> = callbackFlow {
        val startDate = DateTimeUtil.getCurrentDate()
        val endDate = DateTimeUtil.getDateAfter(Constants.DEFAULT_END_DATE_DAYS)

        Api.AsteroidEndpoints.getAllNearingAsteroids(startDate, endDate)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: retrofit2.Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    // Raw string response
                    val stringResponse = response.body()?.string()
                    offer(parseAsteroidsJsonResult(JSONObject(stringResponse ?: "")))
                }

                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, R.string.error_timeout, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, t.message, t)
                }
            })

        awaitClose { this.cancel() }
    }

    private fun saveAsteroids(asteroids: List<Asteroid>) {
        // Save a cached version of all the asteroids in the local DB
        getAppDatabase(applicationContext).asteroidDao.saveNearEarthAsteroids(asteroids)
    }
}