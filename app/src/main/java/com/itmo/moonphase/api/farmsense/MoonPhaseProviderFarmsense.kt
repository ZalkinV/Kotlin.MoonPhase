package com.itmo.moonphase.api.farmsense

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.itmo.moonphase.MoonPhaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.RuntimeException
import java.time.ZonedDateTime

class MoonPhaseProviderFarmsense : MoonPhaseProvider {

    // OkHttp Recipes https://square.github.io/okhttp/recipes/#asynchronous-get-kt-java
    // Using OkHttp https://guides.codepath.com/android/Using-OkHttp
    // Kotlin on Android development: Image download & display using OkHttp https://www.youtube.com/watch?v=HzPmbzIVxDo
    private val httpClient = OkHttpClient()
    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .create()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getMoonPhases(
        startDate: ZonedDateTime,
        endDate: ZonedDateTime) = withContext(Dispatchers.IO) {

        val requestUrlBuilder = Consts.MOON_PHASE_URL.toHttpUrlOrNull()!!.newBuilder()

        var currentDate = startDate
        while (currentDate <= endDate) {
            requestUrlBuilder.addQueryParameter("d[]", currentDate.toEpochSecond().toString())
            currentDate = currentDate.plusDays(1)
        }

        val requestUrl = requestUrlBuilder.build()

        val request = Request.Builder()
            .url(requestUrl)
            .get()
            .build()

        val response = httpClient.newCall(request).execute()

        if (!response.isSuccessful) throw IOException("No response from API")
        val responseText = response.body?.string() ?: throw IOException("No response body from API")
        Log.i("MOON", responseText)

        val moonPhasesResponse = gson.fromJson(responseText, Array<MoonPhaseResponse>::class.java).toList()

        moonPhasesResponse.map { it.toEntity() }
    }

}
