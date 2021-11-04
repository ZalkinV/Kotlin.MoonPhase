package com.itmo.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val currentDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault())

        GlobalScope.launch(Dispatchers.IO) {
            val moonPhases = getMoonPhases(currentDateTime)
            runOnUiThread {
                binding.textView.text = moonPhases[0].toString()
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getMoonPhases(startDate: ZonedDateTime) = withContext(Dispatchers.IO) {
        val requestUrl = MOON_PHASE_URL.toHttpUrlOrNull()!!
            .newBuilder()
            .addQueryParameter("d", startDate.toEpochSecond().toString())
            .build()

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
