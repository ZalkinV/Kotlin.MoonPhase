package com.itmo.moonphase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itmo.moonphase.databinding.ListItemMoonPhaseBinding
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MoonPhaseAdapter(
    private val moonPhases: List<MoonPhaseInfo>,
    context: Context,
) : RecyclerView.Adapter<MoonPhaseAdapter.MoonPhaseViewHolder>() {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.moonPhase_dateTime_format))

    class MoonPhaseViewHolder(private val binding: ListItemMoonPhaseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moonPhaseInfo: MoonPhaseInfo, dateTimeFormatter: DateTimeFormatter) = binding.let { with(moonPhaseInfo) {
            it.tvMoonPhaseDate.text = dateTimeFormatter.format(dateTime)
            it.imgMoonPhase.setImageResource(imageResourceId)
            it.tvMoonPhaseName.text = name
            it.tvMoonPhaseAge.text = age.roundToInt().toString()
            it.tvMoonPhaseIllumination.text = "${(illumination * 100).roundToInt()}%"
        } }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoonPhaseViewHolder {
        val binding = ListItemMoonPhaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)

        return MoonPhaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoonPhaseViewHolder, position: Int) =
        holder.bind(moonPhases[position], dateTimeFormatter)

    override fun getItemCount() = moonPhases.size
}
