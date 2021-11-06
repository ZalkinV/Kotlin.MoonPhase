package com.itmo.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmo.moonphase.api.farmsense.MoonPhaseProviderFarmsense
import com.itmo.moonphase.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.ZoneId

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "MOON_MAIN"
    }

    private lateinit var binding: ActivityMainBinding

    private val moonPhaseProvider: MoonPhaseProvider = MoonPhaseProviderFarmsense()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()

        GlobalScope.launch {
            try {
                val currentDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
                val moonPhases = moonPhaseProvider.getMoonPhases(currentDateTime, currentDateTime.plusDays(Consts.FORECAST_DURATION_DAYS))

                runOnUiThread {
                    binding.rvMoonPhases.adapter = MoonPhaseAdapter(baseContext, moonPhases)
                }
            } catch (e: RuntimeException) {
                e.log(LOG_TAG)
                runOnUiThread {
                    Toast.makeText(baseContext, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
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
