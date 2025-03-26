package com.example.chess.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chess.BackendClasses.Game.Move
import com.example.chess.R

class RVStepsGameAdapter(context: Context, private val steps: MutableList<Move>): RecyclerView.Adapter<RVStepsGameAdapter.StepViewHolder>() {
    inner class StepViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvID = itemView.findViewById<TextView>(R.id.tv_id)
        val tvPosition = itemView.findViewById<TextView>(R.id.tv_step)
        val tvDuration = itemView.findViewById<TextView>(R.id.tv_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_game_step, parent, false)
        return StepViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]
        val stepPos = positionsToStep(step.StartRow, step.StartColumn, step.EndRow, step.EndColumn)
        holder.tvID.text = "${position+1}"
        holder.tvPosition.text = stepPos
        holder.tvDuration.text = step.Duration.split(".")[0]

    }

    override fun getItemCount() = steps.size

    fun positionsToStep(startRow: Int, startColumn: Int, endRow: Int, endColumn: Int): String {
        val symbols = mutableListOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P")
        return "${symbols[startColumn]}$startRow -> ${symbols[endColumn]}$endRow"
    }
}