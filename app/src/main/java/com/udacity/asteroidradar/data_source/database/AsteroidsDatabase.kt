package com.udacity.asteroidradar.data_source.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.data_source.database.dao.AsteroidDao
import com.udacity.asteroidradar.data_source.database.dao.PictureOfDayDao
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay

@Database(
    entities = [Asteroid::class, PictureOfDay::class],
    version = 1,
    exportSchema = false
)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao

    companion object {
        private const val TAG = "AsteroidsDatabase"

        //Application database name
        private const val DB_NAME = "AppData.db"

        @Volatile
        private var INSTANCE: AsteroidsDatabase? = null

        /**
         * Function to get room database instance
         *
         * @param context [Context] instance
         * @return [AsteroidsDatabase.INSTANCE]
         */
        fun getAppDatabase(context: Context): AsteroidsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidsDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}