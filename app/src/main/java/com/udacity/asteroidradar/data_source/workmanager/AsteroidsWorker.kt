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

            fetchAsteroids().collect {
                removeAllAsteroids()
                saveAsteroids(it)
            }

            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    private suspend fun fetchPictureOfDay() {
        try {
            val pictureOfDay = Api.AsteroidEndpoints.getPictureOfDay()
            if (pictureOfDay != null) {
                // Cache the picture of the day
                removePictureOfDay()
                savePictureOfDay(pictureOfDay)
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.message, exception)
        }
    }

    private fun removePictureOfDay() {
        getAppDatabase(applicationContext).pictureOfDayDao.deletePictureOfDay()
    }

    private fun savePictureOfDay(pictureOfDay: PictureOfDay) {
        getAppDatabase(applicationContext).pictureOfDayDao.savePictureOfDay(pictureOfDay)
    }

    private fun fetchAsteroids(): Flow<List<Asteroid>> = callbackFlow {
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
                    Toast.makeText(applicationContext, R.string.error_timeout, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, t.message, t)
                }
            })

        awaitClose { this.cancel() }
    }

    private fun removeAllAsteroids() {
        // Remove all the asteroids in the local DB
        getAppDatabase(applicationContext).asteroidDao.removeAllAsteroids()
    }

    private fun saveAsteroids(asteroids: List<Asteroid>) {
        // Save a cached version of all the asteroids in the local DB
        getAppDatabase(applicationContext).asteroidDao.saveNearEarthAsteroids(asteroids)
    }
}