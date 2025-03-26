package com.example.chess.Activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chess.Adapters.RVPlayerInfoAdapter
import com.example.chess.Adapters.RVStepsGameAdapter
import com.example.chess.BackendClasses.Game.Move
import com.example.chess.CustomElements.MySpinner
import com.example.chess.Functions.NetworkFunctions
import com.example.chess.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PopupActivities(private val context: Context) {

    val networkFunctions = NetworkFunctions(context)

    fun createGame() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_game_create)
        dialog.show()

        val etGameName = dialog.findViewById<EditText>(R.id.et_game_name)
        val chip2 = dialog.findViewById<TextView>(R.id.tv_2)
        val chip4 = dialog.findViewById<TextView>(R.id.tv_4)
        val cbPrivateGame = dialog.findViewById<CheckBox>(R.id.cb_private_game)
        val cbPrekol = dialog.findViewById<CheckBox>(R.id.cb_prekol)
        val sMode = dialog.findViewById<MySpinner>(R.id.s_mode_game)
        val bJoid = dialog.findViewById<Button>(R.id.b_create)

        val modes = context.resources.getStringArray(R.array.mode_names)
        val adapterLessons = ArrayAdapter(context.applicationContext, R.layout.spinner_item, modes)
        var playersNum = 2

        adapterLessons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        chip4.setOnClickListener {
            chip2.setBackgroundColor(context.getColor(R.color.full_transparant))
            chip2.setTextColor(context.getColor(R.color.light_black))
            chip4.background = context.getDrawable(R.drawable.blue_button)
            chip4.setTextColor(context.getColor(R.color.white))
            playersNum = 4
        }
        chip2.setOnClickListener {
            chip4.setBackgroundColor(context.getColor(R.color.full_transparant))
            chip4.setTextColor(context.getColor(R.color.light_black))
            chip2.background = context.getDrawable(R.drawable.blue_button)
            chip2.setTextColor(context.getColor(R.color.white))
            playersNum = 2
        }

        sMode.adapter = adapterLessons
        sMode.setSelection(0)

        bJoid.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    networkFunctions.gameCreate(etGameName.text.toString(), playersNum, cbPrivateGame.isChecked, sMode.selectedItem.toString(), cbPrekol.isChecked)
                } catch (e:Exception) {
                    Toast.makeText(context, "Бэк накрылся", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
    }

    fun joinGame() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_game_join)
        dialog.show()

        val etGameCode = dialog.findViewById<EditText>(R.id.et_game_code)
        val bJoin = dialog.findViewById<Button>(R.id.b_join)

        bJoin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                networkFunctions.gameJoin(etGameCode.text.toString())
                } catch (e:Exception) {
                Toast.makeText(context, "Бэк накрылся", Toast.LENGTH_SHORT).show()
        }
            }
            dialog.dismiss()
        }
    }

    fun changePassword() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_change_password)
        dialog.show()

        val etOldPassword = dialog.findViewById<EditText>(R.id.et_old_password)
        val etNewPassword = dialog.findViewById<EditText>(R.id.et_new_password)
        val bSubmit = dialog.findViewById<Button>(R.id.b_submit)

        bSubmit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val res = networkFunctions.changePassword(etOldPassword.text.toString(), etNewPassword.text.toString())
                    if (res) {
                        Toast.makeText(context, "Пароль сменён", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                    }
                } catch (e:Exception) {
                    Toast.makeText(context, "Бэк накрылся", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
    }

    fun endGameView(gameResult: Boolean, league: String, farmPoints: Int, pointsForWin: Int, itog: Int, rangPoints: Int) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_end_game)
        dialog.show()

        val tvGameResult = dialog.findViewById<TextView>(R.id.tv_game_result)
        val tvLeague = dialog.findViewById<TextView>(R.id.tv_league)
        val tvFarmPonts = dialog.findViewById<TextView>(R.id.tv_farm_points)
        val tvPointsForWin = dialog.findViewById<TextView>(R.id.points_for_win)
        val tvItog = dialog.findViewById<TextView>(R.id.tv_itog)
        val tvRangPoints = dialog.findViewById<TextView>(R.id.tv_rang_points)
        val bSubmit = dialog.findViewById<Button>(R.id.b_exit)

        tvGameResult.text = if(gameResult) context.getString(R.string.win) else context.getString(R.string.lose)
        tvLeague.text = league
        tvFarmPonts.text = farmPoints.toString()
        tvPointsForWin.text = pointsForWin.toString()
        tvItog.text = itog.toString()
        tvRangPoints.text = rangPoints.toString()

        bSubmit.setOnClickListener {

            dialog.dismiss()
        }
    }

    fun gameSteps(steps: MutableList<Move>) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.popup_game_steps)
        dialog.show()

        val rvGameSteps = dialog.findViewById<RecyclerView>(R.id.rv_steps)

        rvGameSteps.layoutManager = LinearLayoutManager(context.applicationContext)
        rvGameSteps.adapter = RVStepsGameAdapter(context, steps)
    }
}