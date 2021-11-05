package com.itmo.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmo.moonphase.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val moonPhaseProvider: MoonPhaseProvider = MoonPhaseProviderFarmsense()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()

        GlobalScope.launch(Dispatchers.IO) {
            val currentDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
            val moonPhases = moonPhaseProvider.getMoonPhases(currentDateTime, currentDateTime.plusDays(Consts.FORECAST_DURATION_DAYS))

            runOnUiThread {
                binding.rvMoonPhases.adapter = MoonPhaseAdapter(baseContext, moonPhases)
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

}
