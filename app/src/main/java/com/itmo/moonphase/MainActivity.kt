package com.itmo.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.itmo.moonphase.Consts.MOON_PHASE_URL
import com.itmo.moonphase.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
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

        val requestUrl = MOON_PHASE_URL.toHttpUrlOrNull()!!
            .newBuilder()
            .addQueryParameter("d", System.currentTimeMillis().toString())
            .build()

        val request = Request.Builder()
            .url(requestUrl)
            .get()
            .build()

        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body?.string() ?: "No response"
                Log.i("MOON", responseText)
                
                runOnUiThread {
                    binding.textView.text = responseText
                }
            }
        })
    }
}
