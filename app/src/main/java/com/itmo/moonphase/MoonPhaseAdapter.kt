package com.itmo.moonphase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itmo.moonphase.databinding.ListItemMoonPhaseBinding
import kotlin.math.roundToInt

class MoonPhaseAdapter(private val moonPhases: List<MoonPhaseInfo>) : RecyclerView.Adapter<MoonPhaseAdapter.MoonPhaseViewHolder>() {

    class MoonPhaseViewHolder(private val binding: ListItemMoonPhaseBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moonPhaseInfo: MoonPhaseInfo) = binding.let { with(moonPhaseInfo) {
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
        holder.bind(moonPhases[position])

    override fun getItemCount() = moonPhases.size
}
