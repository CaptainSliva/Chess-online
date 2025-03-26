package com.example.chess.Activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chess.Adapters.RVListgamesAdapter
import com.example.chess.BackendClasses.Profile.PublicGame
import com.example.chess.BackendClasses.Profile.allPublicGames
import com.example.chess.CustomElements.SpacingItemDecoration
import com.example.chess.Datastore.DataDS
import com.example.chess.Datastore.FunctionsDS
import com.example.chess.Datastore.KeysDS
import com.example.chess.Functions.FunctionsApp
import com.example.chess.Functions.NetworkFunctions
import com.example.chess.HistoryGamesActivity
import com.example.chess.R
import com.example.chess.dataStore
import com.example.chess.databinding.ActivityMainBinding
import com.example.chess.token
import com.example.chess.userID
import com.example.chess.wsL
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding
    lateinit var games: allPublicGames

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {

            val userPreferencesFlow: Flow<DataDS> = dataStore.data.map { preferences ->
                DataDS(preferences[KeysDS.TOKEN].toString(), preferences[KeysDS.ID].toString())
            }

            userPreferencesFlow.collect { data ->
                println("token - ${data.token}\n id - ${data.id}")
                if (data.token == "null") {
                    val intent = Intent(applicationContext, RegistrationActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    token = data.token
                    userID = data.id
                }
            }
        }
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bCreate = binding.bCreateGame
        val bJoin = binding.bJoin
        val bWar = binding.bWar
        val bSettings = binding.ibSettings
        val tvLeague = binding.tvLeague
        val tvLevel = binding.tvLevel
        val tvScore = binding.tvScore
        val rvListGames = binding.rvListgames
        val ibRefresh = binding.refreshGames

        var visibleButtons = false

        wsL.connectToWS()
        val functions = FunctionsApp(this)
        val networkFunctions = NetworkFunctions(this)
        val popupActivities = PopupActivities(this)

        rvListGames.addItemDecoration(SpacingItemDecoration(functions.dpToPx(8)))
        rvListGames.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rvListGames.adapter = RVListgamesAdapter(applicationContext, mutableListOf(PublicGame("Поиск игр", "-1", 0, 0, " ", false)))

        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(500)
            val playerData = networkFunctions.getPlayerData()
            games = networkFunctions.getAllPublicGames()!!

            runOnUiThread {
                tvLeague.text = playerData.league
                tvLevel.text = "${getString(R.string.level)}${playerData.level}"
                tvScore.text = playerData.score.toString()
                rvListGames.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                rvListGames.adapter = RVListgamesAdapter(applicationContext, games)
            }
        }

        bSettings.setOnClickListener {
            val popup = PopupMenu(this, bSettings)
            popup.inflate(R.menu.settings)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.logout -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            FunctionsDS(applicationContext).saveToDataStore("null", "null")
                            val intent = Intent(applicationContext, AutorizationActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        true
                    }

                    R.id.change_password -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            popupActivities.changePassword()
                        }
                        true
                    }

                    R.id.account_delete -> {
                        var alertDialog: AlertDialog? = null
                        val alertDialogBuilder = AlertDialog.Builder(applicationContext)
                        alertDialogBuilder.setTitle("Удалтть аккаунт")
                        alertDialogBuilder.setMessage("Вы хотите удалить аккаунт?")
                        alertDialogBuilder.setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val res = networkFunctions.accountDelete()
                                runOnUiThread {
                                    if (res) Toast.makeText(applicationContext, "Аккаунт удалён", Toast.LENGTH_SHORT).show()
                                    else Toast.makeText(applicationContext, "Ошибка", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(applicationContext, AutorizationActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                        alertDialogBuilder.setNegativeButton("Нет") { dialogInterface: DialogInterface, i: Int -> Toast.makeText(applicationContext, "Отмена", Toast.LENGTH_SHORT).show()}
                        alertDialog = alertDialogBuilder.create()
                        alertDialog.show()

                        true
                    }

                    R.id.history -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val intent = Intent(applicationContext, HistoryGamesActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
            popup.show()
        }

        bCreate.setOnClickListener {
            popupActivities.createGame()
        }

        bJoin.setOnClickListener {
            popupActivities.joinGame()
        }

        bWar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                networkFunctions.getRandomGame()
                runOnUiThread {
                    functions.showSnackBar(bWar, "Предупреждение", resources.getColor(R.color.yellow), Snackbar.LENGTH_SHORT, true, "За шо")
                }
            }
        }

        ibRefresh.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                games = networkFunctions.getAllPublicGames()!!
                runOnUiThread {
//                    rvListGames.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false) // getAllPublicGames делать каждую минуту запрос
                    rvListGames.adapter = RVListgamesAdapter(applicationContext, games)
                    rvListGames.scrollToPosition(games.size-1)
                }
            }
        }
    }


}

