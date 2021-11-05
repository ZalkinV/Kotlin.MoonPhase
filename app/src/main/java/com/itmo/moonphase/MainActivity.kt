package com.itmo.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.itmo.moonphase.Consts.MOON_PHASE_URL
import com.itmo.moonphase.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // OkHttp Recipes https://square.github.io/okhttp/recipes/#asynchronous-get-kt-java
    // Using OkHttp https://guides.codepath.com/android/Using-OkHttp
    // Kotlin on Android development: Image download & display using OkHttp https://www.youtube.com/watch?v=HzPmbzIVxDo
    private val httpClient = OkHttpClient()
    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()

        val currentDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault())

        GlobalScope.launch(Dispatchers.IO) {
            val moonPhases = getMoonPhases(currentDateTime, currentDateTime.plusDays(Consts.FORECAST_DURATION_DAYS))

            runOnUiThread {
                binding.rvMoonPhases.adapter = MoonPhaseAdapter(
                    moonPhases.map { MoonPhaseInfo(it.phase, 0.0, it.illumination) })
            }
        }
    }

    private fun initializeComponents() {
        // RecyclerView https://www.youtube.com/watch?v=cDF_yBCflXk
        // RecyclerView with ViewBinding https://dev.to/theimpulson/using-recyclerview-with-viewbinding-in-android-via-kotlin-1hgl
        binding.rvMoonPhases.apply {
            layoutManager = LinearLayoutManager(baseContext)
            setHasFixedSize(true)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getMoonPhases(
        startDate: ZonedDateTime,
        endDate: ZonedDateTime = startDate) = withContext(Dispatchers.IO) {

        val requestUrlBuilder = MOON_PHASE_URL.toHttpUrlOrNull()!!.newBuilder()

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
        val responseText = response.body?.string() ?: "No response"
        Log.i("MOON", responseText)

        gson.fromJson(responseText, Array<MoonPhaseResponse>::class.java).toList()
    }
}
