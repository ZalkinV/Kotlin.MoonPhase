package com.itmo.moonphase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itmo.moonphase.databinding.ListItemMoonPhaseBinding
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MoonPhaseAdapter(
    context: Context,
    private val moonPhases: List<MoonPhaseInfo>,
) : RecyclerView.Adapter<MoonPhaseAdapter.MoonPhaseViewHolder>() {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.moonPhase_dateTime_format))
    private val percentFormatter = NumberFormat.getPercentInstance().apply { minimumFractionDigits = 0 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoonPhaseViewHolder {
        val binding = ListItemMoonPhaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        return MoonPhaseViewHolder(binding, dateTimeFormatter, percentFormatter)
    }

    override fun onBindViewHolder(holder: MoonPhaseViewHolder, position: Int) =
        holder.bind(moonPhases[position])

    override fun getItemCount() = moonPhases.size

    class MoonPhaseViewHolder(
        private val binding: ListItemMoonPhaseBinding,
        private val dateTimeFormatter: DateTimeFormatter,
        private val percentFormatter: NumberFormat,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moonPhaseInfo: MoonPhaseInfo) = binding.let { with(moonPhaseInfo) {
            MoonPhaseResource.getMoonPhaseResource(phase).let { mpr ->
                it.imgMoonPhase.setImageResource(mpr.imageId)
                it.tvMoonPhaseName.text = it.root.context.getString(mpr.nameId)
            }

            it.tvMoonPhaseDate.text = dateTimeFormatter.format(dateTime)
            it.tvMoonPhaseAge.text = age.roundToInt().toString()
            it.tvMoonPhaseIllumination.text = percentFormatter.format(illumination)
        } }

    }
}
