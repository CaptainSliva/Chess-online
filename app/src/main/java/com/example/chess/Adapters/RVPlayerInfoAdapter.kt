package com.example.chess.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.chess.BackendClasses.Game.Board
import com.example.chess.BackendClasses.Game.GameData
import com.example.chess.BackendClasses.Game.Player
import com.example.chess.R
import com.example.chess.userID

class RVPlayerInfoAdapter(private val context: Context, private val playersInfo: MutableList<Player>): RecyclerView.Adapter<RVPlayerInfoAdapter.PlayerViewHolder>() {
    inner class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.card)
        val time = itemView.findViewById<TextView>(R.id.player_time)
        val name = itemView.findViewById<TextView>(R.id.player_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_player, parent, false)
        return PlayerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {

        val player = playersInfo[position]

        if (player.Color == "#2DF600") {
            holder.card.setCardBackgroundColor(context.getColor(R.color.green))
//            holder.card.setCardBackgroundColor(Color.parseColor(player.Color))
            println(player.Color)
        }
        else {
            holder.card.setCardBackgroundColor(context.getColor(R.color.card_color))
            println(player.Color)
        }
        holder.time.text = player.Time
        holder.name.text = player.Nickname

    }

    override fun getItemCount() = playersInfo.size



}