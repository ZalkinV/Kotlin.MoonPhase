package com.itmo.moonphase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.itmo.moonphase.api.farmsense.MoonPhaseProviderFarmsense
import com.itmo.moonphase.databinding.ActivityMainBinding
import com.itmo.moonphase.workers.MoonPhaseWorker
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

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
        initializeWorkers()

        lifecycleScope.launch {
            try {
                val currentDateTime = getDateTimeNow()
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

    // Define work requests: Schedule periodic work https://developer.android.com/topic/libraries/architecture/workmanager/how-to/define-work#schedule_periodic_work
    private fun initializeWorkers() {
        val request = PeriodicWorkRequestBuilder<MoonPhaseWorker>(Consts.WORKER_PERIOD_HOURS, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(baseContext)
            .enqueue(request)
    }

}
