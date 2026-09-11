package com.example.s8189084assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s8189084assignment2.R
import com.example.s8189084assignment2.data.model.Sport

// Turns a list of Sport into RecyclerView cards, onSportClick fires when a card is tapped.
class SportAdapter(
    private val onSportClick: (Sport) -> Unit
) : RecyclerView.Adapter<SportAdapter.SportViewHolder>() {

    private var sports: List<Sport> = emptyList()

    // Replaces the current list and redraws the RecyclerView.
    fun submitList(newSports: List<Sport>) {
        sports = newSports
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sport, parent, false)
        return SportViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
        holder.bind(sports[position], onSportClick)
    }

    override fun getItemCount(): Int = sports.size

    class SportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameText: TextView = itemView.findViewById(R.id.sportNameText)
        private val playerCountText: TextView = itemView.findViewById(R.id.playerCountText)
        private val fieldTypeText: TextView = itemView.findViewById(R.id.fieldTypeText)
        private val badgeText: TextView = itemView.findViewById(R.id.badgeText)

        fun bind(sport: Sport, onSportClick: (Sport) -> Unit) {
            nameText.text = sport.sportName
            playerCountText.text =
                itemView.context.getString(R.string.dashboard_player_count, sport.playerCount)
            fieldTypeText.text = sport.fieldType

            // Swap the badge text and background depending on the Olympic status.
            if (sport.olympicSport) {
                badgeText.text = itemView.context.getString(R.string.badge_olympic)
                badgeText.setBackgroundResource(R.drawable.bg_badge_olympic)
                badgeText.setTextColor(itemView.context.getColor(R.color.primary_blue))
            } else {
                badgeText.text = itemView.context.getString(R.string.badge_not_olympic)
                badgeText.setBackgroundResource(R.drawable.bg_badge_not_olympic)
                badgeText.setTextColor(itemView.context.getColor(R.color.text_secondary))
            }

            itemView.setOnClickListener { onSportClick(sport) }
        }
    }
}
