package com.example.chess.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chess.BackendClasses.Game.Board
import com.example.chess.BackendClasses.Game.GameData
import com.example.chess.BackendClasses.Game.MovePiece
import com.example.chess.BackendClasses.Game.PieceType
import com.example.chess.Functions.ChessFunctionsBase
import com.example.chess.Functions.FunctionsApp
import com.example.chess.Functions.NetworkFunctions
import com.example.chess.R
import com.example.chess.gameID
import com.example.chess.token
import com.google.android.material.navigation.NavigationBarItemView
import com.google.android.material.snackbar.Snackbar

class RVFigureAdapter(context: Context, private val listFigures: MutableList<MutableList<Board>>): RecyclerView.Adapter<RVFigureAdapter.Figure>() {
    val chessBase = ChessFunctionsBase(context)
    val gridSize = listFigures.size
    var figureMove = false
    var figureClick = false
    var previousRow = -1
    var previousCol = -1
    var previousPos = -1
    inner class Figure(itemView: View, figures: MutableList<MutableList<Board>>) : RecyclerView.ViewHolder(itemView) {
        val figure = itemView.findViewById<View>(R.id.figure)

        init {
            val functionsApp = FunctionsApp(itemView.context)
            figure.setOnClickListener {
                val row = adapterPosition/gridSize
                val col = adapterPosition-row*gridSize
                val pos = adapterPosition
                println("adpos $adapterPosition\n row$row | col$col")
                if (figures[row][col]?.Type != null && previousPos == -1) {
                    previousRow = row
                    previousCol = col
                    previousPos = adapterPosition
                    figureClick = true
                    functionsApp.showSnackBar(figure, "Взял фигуру", itemView.context.getColor(R.color.card_color), 1, false)
                }
                if (figureMove) {
                    if (previousRow == row && previousCol == col) {
                        functionsApp.showSnackBar(figure, "Поставил фигуру", itemView.context.getColor(R.color.card_color), 1, false)
                    }
                    else {
                        val message = MovePiece(
                            "MovePiece",
                            gameID,
                            token,
                            previousRow,
                            previousCol,
                            row,
                            col
                        )
                        NetworkFunctions(itemView.context).sendMessageToServer(message)
                        functionsApp.showSnackBar(figure, "Переместил фигуру", itemView.context.getColor(R.color.card_color), 1, false)
                    }
                    previousPos = -1
                    figureMove = false
                }
                if (figureClick) {
                    figureMove = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Figure {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_figure, parent, false)
        return Figure(itemView, listFigures)
    }

    override fun onBindViewHolder(holder: Figure, position: Int) {

        val n = position/gridSize
        println(position)
        val currentSquare = listFigures[n][position-n*gridSize]
        if (currentSquare?.Type != null) {
            println("figure type ${currentSquare.Type} ; figure color ${currentSquare.Color}")
            holder.figure.background = chessBase.colorFigure(currentSquare.Type, currentSquare.Color)
        }


    }

    override fun getItemCount() = listFigures.size*listFigures.size
}