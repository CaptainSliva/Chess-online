package com.example.chess.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.chess.BackendClasses.Profile.historyGames
import com.example.chess.R

class RVArhiveAdapter(private val context: Context, private val games: historyGames): RecyclerView.Adapter<RVArhiveAdapter.GameViewHolder>() {
    inner class GameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.card)
        val tvGameName = itemView.findViewById<TextView>(R.id.game_name)
        val tvGameTime = itemView.findViewById<TextView>(R.id.game_time)
        val tvGamePlayers = itemView.findViewById<TextView>(R.id.game_players)
        val tvGameType = itemView.findViewById<TextView>(R.id.game_type)
        val tvGameCreateTime = itemView.findViewById<TextView>(R.id.game_create_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_history_game, parent, false)
        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        if (!game.isWinner) {
            holder.card.setCardBackgroundColor(context.getColor(R.color.light_red))
        }
        holder.tvGameName.text = game.gameName
        holder.tvGameTime.text = game.gameDuration
        holder.tvGamePlayers.text = game.playerCount.toString()
        holder.tvGameType.text = game.gameMode
        holder.tvGameCreateTime.text = game.dateCreated.split("T")[0]
    }

    override fun getItemCount() = games.size
}