package com.example.chess.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.chess.R
import com.example.chess.Other.Square

class AdapterFightArea(
    context: Context,
    fightArray: MutableList<Square>
)
    : ArrayAdapter<Square?>(context, 0, fightArray as List<Square?>) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var item = view
        if (item == null) {
            item = LayoutInflater.from(context).inflate(R.layout.fight_square, parent, false)
        }

        val model = getItem(position)
        val square = item!!.findViewById<View>(R.id.square)
        square.background = model?.resColor
        square.setBackgroundDrawable(model?.resColor)

        return item
    }

}