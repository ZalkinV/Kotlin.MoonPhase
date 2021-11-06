package com.itmo.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmo.moonphase.api.farmsense.MoonPhaseProviderFarmsense
import com.itmo.moonphase.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.time.ZoneId

// Android Kotlin Coroutine Scope for Activity, Fragment and ViewModel (Architecture Components): https://code.luasoftware.com/tutorials/android/android-kotlin-coroutine-scope-for-activity/
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

        lifecycleScope.launch {
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
