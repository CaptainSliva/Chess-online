package com.example.chess.Functions

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat
import com.example.chess.BackendClasses.Game.PieceType
import com.example.chess.Other.Square
import com.example.chess.R
import com.example.chess.numColumnsGame

class ChessFunctionsBase(private val context: Context) {

    fun createCommonChessBattleArena() : MutableList<Square> {

        val fBase = ChessFunctionsBase(context)
        val fightArena = mutableListOf<Square>()

        for (i in 0..<numColumnsGame) {
            for (j in 0..<numColumnsGame) {
                val resultSquare = LayerDrawable(arrayOf(fBase.colorSquare(i + j)))
                fightArena.add(Square(i, j, resultSquare))
            }
        }
        println("farea = ${fightArena.size}")
        return fightArena
    }

    fun colorSquare(i: Int): Drawable? {
        return when {
            i%2 == 0 -> ContextCompat.getDrawable(context, R.drawable.square_black)
            else -> ContextCompat.getDrawable(context, R.drawable.square_white)
        }
    }
    fun colorFigure(type: PieceType, color: String): Drawable? {
        val color = when(color) {
            "#eeeeee" -> context.getColor(R.color.figure_white)
            "#000000" -> context.getColor(R.color.figure_black)
            "#DD4CEE" -> context.getColor(R.color.figure_pink)
            "#7074D5" -> context.getColor(R.color.figure_blue)
            else -> context.getColor(R.color.super_red)
        }
//        val color = Color.parseColor(color)
        val figure = when {
            type == PieceType.King -> {
                ContextCompat.getDrawable(context, R.drawable.king)
            }
            type == PieceType.Queen -> {
                ContextCompat.getDrawable(context, R.drawable.queen)
            }
            type == PieceType.Rook -> {
                ContextCompat.getDrawable(context, R.drawable.castle)
            }
            type == PieceType.Bishop -> {
                ContextCompat.getDrawable(context, R.drawable.elephant)
            }
            type == PieceType.Knight -> {
                ContextCompat.getDrawable(context, R.drawable.horse)
            }
            type == PieceType.Pawn -> {
                ContextCompat.getDrawable(context, R.drawable.pawn)
            }
            else -> {
                ContextCompat.getDrawable(context, R.drawable.alarm_24)
            }
        }
        figure?.setTint(color)
        return figure
    }

}