package com.udacity.asteroidradar.data_source.web

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.data_source.web.api.AsteroidsApi
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.nasa.gov/"
const val API_KEY =
    "TVGhdaeB9KcZ77oGxf2duYJBmuHr30X5D8adcSuT"//You can use 'DEMO_KEY' or please generate a key from (https://api.nasa.gov/) and paste it here

private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val client: OkHttpClient = OkHttpClient().newBuilder().apply {
    readTimeout(40, TimeUnit.SECONDS)
    connectTimeout(40, TimeUnit.SECONDS)

    addInterceptor { chain ->
        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        // Request customization: add request headers
        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)
        val request: Request = requestBuilder.build()
        chain.proceed(request)
    }
}.addInterceptor(loggingInterceptor).build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(client)
    .build()

object Api {
    val AsteroidEndpoints: AsteroidsApi by lazy {
        retrofit.create(AsteroidsApi::class.java)
    }
}