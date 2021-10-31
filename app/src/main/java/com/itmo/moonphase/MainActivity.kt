package com.itmo.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.itmo.moonphase.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    // OkHttp Recipes https://square.github.io/okhttp/recipes/#asynchronous-get-kt-java
    // Using OkHttp https://guides.codepath.com/android/Using-OkHttp
    // Kotlin on Android development: Image download & display using OkHttp https://www.youtube.com/watch?v=HzPmbzIVxDo
    private val httpClient = OkHttpClient()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val request = Request.Builder()
            .url(Consts.MOON_PHASE_URL)
            .get()
            .build()

        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i("MOON", response.body?.string() ?: "")
            }
        })
    }
}
