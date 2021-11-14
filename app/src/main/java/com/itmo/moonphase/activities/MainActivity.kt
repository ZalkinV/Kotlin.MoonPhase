package com.itmo.moonphase.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmo.moonphase.*
import com.itmo.moonphase.api.farmsense.MoonPhaseProviderFarmsense
import com.itmo.moonphase.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.lang.RuntimeException

// Android Kotlin Coroutine Scope for Activity, Fragment and ViewModel (Architecture Components): https://code.luasoftware.com/tutorials/android/android-kotlin-coroutine-scope-for-activity/
class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "MOON_MAIN"
    }

    private lateinit var binding: ActivityMainBinding

    private val moonPhaseProvider: MoonPhaseProvider = MoonPhaseProviderFarmsense()
    private lateinit var moonPhaseAdapter: MoonPhaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeComponents()

        lifecycleScope.launch {
            try {
                val currentDateTime = getDateTimeNow()
                val moonPhases = moonPhaseProvider.getMoonPhases(currentDateTime, currentDateTime.plusDays(Consts.FORECAST_DURATION_DAYS))

                moonPhaseAdapter.update(moonPhases)
            } catch (e: RuntimeException) {
                e.log(LOG_TAG)
                runOnUiThread {
                    Toast.makeText(baseContext, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initializeComponents() = with(binding) {
        moonPhaseAdapter = MoonPhaseAdapter(baseContext)

        // RecyclerView https://www.youtube.com/watch?v=cDF_yBCflXk
        // RecyclerView with ViewBinding https://dev.to/theimpulson/using-recyclerview-with-viewbinding-in-android-via-kotlin-1hgl
        with(rvMoonPhases) {
            layoutManager = LinearLayoutManager(baseContext)
            setHasFixedSize(true)
            adapter = moonPhaseAdapter
        }
    }

    // How to set menu to Toolbar in Android https://stackoverflow.com/a/50225618
    // Menus https://developer.android.com/guide/topics/ui/menus#kotlin
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_options_settings -> {
            openSettingsActivity()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun openSettingsActivity() {
        val intent = Intent(baseContext, SettingsActivity::class.java)
        startActivity(intent)
    }

}
