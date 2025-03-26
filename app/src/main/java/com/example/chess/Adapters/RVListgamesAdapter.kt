package com.example.chess.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chess.BackendClasses.Profile.allPublicGames
import com.example.chess.Functions.NetworkFunctions
import com.example.chess.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RVListgamesAdapter(private val context: Context, private val games: allPublicGames) : RecyclerView.Adapter<RVListgamesAdapter.GameViewHolder>() {
    inner class GameViewHolder(itemView: View, games: allPublicGames) : RecyclerView.ViewHolder(itemView) {
        val tvGameName = itemView.findViewById<TextView>(R.id.game_name)
        val tvType = itemView.findViewById<TextView>(R.id.game_type)
        val tvPlayersCount = itemView.findViewById<TextView>(R.id.player_count)
        val tvPlayers = itemView.findViewById<TextView>(R.id.total_players)
        val bStart = itemView.findViewById<Button>(R.id.game_start)

        init {
            bStart.setOnClickListener {
                if (games[adapterPosition].gameId != "-1") {
                    CoroutineScope(Dispatchers.IO).launch {
                        NetworkFunctions(context).gameJoin(games[adapterPosition].gameId)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_game, parent, false)
        return GameViewHolder(itemView, games)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.tvGameName.text = game.title
        holder.tvType.text = "${context.getString(R.string.game_type)}${game.gameMode}"
        holder.tvPlayersCount.text = "${game.totalPlayers}${context.getString(R.string.players_count)}"
        holder.tvPlayers.text = "${context.getString(R.string.players)}${game.playerCount}"
    }

    override fun getItemCount() = games.size



}
