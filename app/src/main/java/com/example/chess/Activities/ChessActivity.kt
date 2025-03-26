package com.example.chess.Activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chess.Adapters.AdapterFightArea
import com.example.chess.Adapters.RVFigureAdapter
import com.example.chess.Adapters.RVPlayerInfoAdapter
import com.example.chess.BackendClasses.Game.AddPerson
import com.example.chess.BackendClasses.Game.Board
import com.example.chess.BackendClasses.Game.DurationGame
import com.example.chess.BackendClasses.Game.FinishGame
import com.example.chess.BackendClasses.Game.GameOverPlayer
import com.example.chess.BackendClasses.Game.GameStateMessage
import com.example.chess.BackendClasses.Game.JoinTheGame
import com.example.chess.BackendClasses.Game.KillAllPiece
import com.example.chess.BackendClasses.Game.Move
import com.example.chess.BackendClasses.Game.NewMove
import com.example.chess.BackendClasses.Game.PieceType
import com.example.chess.BackendClasses.Game.Player
import com.example.chess.BackendClasses.Game.PlayerIsActive
import com.example.chess.BackendClasses.Game.RemainingTimePerson
import com.example.chess.BackendClasses.Game.RemovePlayer
import com.example.chess.BackendClasses.Game.ResultJoinTheGame
import com.example.chess.BackendClasses.Game.ResultPlayerTheGame
import com.example.chess.BackendClasses.Game.ReversTimeAnActivePlayer
import com.example.chess.BackendClasses.Game.ReverseTimer
import com.example.chess.BackendClasses.Game.UpdateBoard
import com.example.chess.BackendClasses.Game.UpdateColorPlayer
import com.example.chess.BackendClasses.Game.UpdateScore
import com.example.chess.BackendClasses.Game.UsePotion
import com.example.chess.Functions.ChessFunctionsBase
import com.example.chess.Functions.FunctionsApp
import com.example.chess.Functions.NetworkFunctions
import com.example.chess.R
import com.example.chess.gameID
import com.example.chess.numColumnsGame
import com.example.chess.userID
import com.example.chess.wsL
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ChessActivity : AppCompatActivity() {

    lateinit var tvGameID: TextView
    lateinit var tvTime: TextView
    lateinit var tvGameName: TextView
    lateinit var tvStart: TextView
    lateinit var tvPlayerNameToJoin: TextView
    lateinit var tvPoints: TextView
    lateinit var tvPawnKilled: TextView
    lateinit var tvHorseKilled: TextView
    lateinit var tvElephantKilled: TextView
    lateinit var tvCastleKilled: TextView
    lateinit var tvQueenKilled: TextView
    lateinit var tvKingKilled: TextView

    lateinit var joinLayout: LinearLayout
    lateinit var ivPlayerColor: ImageView
    lateinit var bYes: Button
    lateinit var bNo: Button

    lateinit var gridFight: GridView
    lateinit var rvPlayersInfo: RecyclerView
    lateinit var rvGameFigures: RecyclerView

    lateinit var playersList: MutableList<Player>
    lateinit var gameBoard: MutableList<MutableList<Board>>
    lateinit var userWantToJoin: String
    var show1 = true
    var isInitialize = false
    val gameSteps = mutableListOf<Move>()




    lateinit var networkFunctions: NetworkFunctions
    lateinit var functions: FunctionsApp
    lateinit var baseFunctionsChess: ChessFunctionsBase
    lateinit var popupActivities: PopupActivities

    var a = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        wsL.initialize(this)
        wsL.subscribe { mes ->
            handleWebSocketMessage(mes)
        }

        networkFunctions = NetworkFunctions(this)
        functions = FunctionsApp(this)
        baseFunctionsChess = ChessFunctionsBase(this)
        popupActivities = PopupActivities(this)

        enableEdgeToEdge()
        setContentView(R.layout.activity_chess)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        gridFight = findViewById<GridView>(R.id.gv_game_square)
        rvPlayersInfo = findViewById(R.id.rv_players_info)
        rvGameFigures = findViewById(R.id.rv_game_figures)

        tvGameID = findViewById<TextView>(R.id.tv_game_id)
        tvTime = findViewById(R.id.tv_game_time)
        tvGameName = findViewById(R.id.tv_game_name)
        tvStart = findViewById(R.id.tv_for_start)
        tvPlayerNameToJoin = findViewById(R.id.tv_player_name)
        tvPoints = findViewById(R.id.tv_points)
        tvPawnKilled = findViewById(R.id.tv_pawn_killed)
        tvHorseKilled = findViewById(R.id.tv_horse_killed)
        tvElephantKilled = findViewById(R.id.tv_elephant_killed)
        tvCastleKilled = findViewById(R.id.tv_castle_killed)
        tvQueenKilled = findViewById(R.id.tv_queen_killed)
        tvKingKilled = findViewById( R.id.tv_king_killed)

        joinLayout = findViewById(R.id.linear_join_request)
        bYes = findViewById(R.id.b_yes)
        bNo = findViewById(R.id.b_no)
        ivPlayerColor = findViewById(R.id.iv_player_color)
        val bSteps = findViewById<Button>(R.id.b_steps)
        val bEnd = findViewById<Button>(R.id.b_game_end)


        CoroutineScope(Dispatchers.IO).launch {
            val gameField = networkFunctions.getPlayingField(gameID)
            if (gameField.Board != null) {
                numColumnsGame = gameField.Board.size
                runOnUiThread {
                    tvGameID.text = gameID
                    tvGameName.text = gameField.GameName
                    rvGameFigures.layoutManager = GridLayoutManager(this@ChessActivity, gameField.Board.size)
                    gameBoard = gameField.Board
                    rvGameFigures.adapter = RVFigureAdapter(this@ChessActivity, gameBoard)

                    // gridFight
                    val fightArena = baseFunctionsChess.createCommonChessBattleArena()
                    gridFight.numColumns = numColumnsGame
                    val fightAdapter = AdapterFightArea(this@ChessActivity, fightArena)
                    gridFight.adapter = fightAdapter

                    rvPlayersInfo.layoutManager = LinearLayoutManager(applicationContext)
                    playersList = gameField.Players
                    rvPlayersInfo.adapter = RVPlayerInfoAdapter(this@ChessActivity, gameField.Players)
                    gameField.Players.forEach {
                        if (it.PlayerId == userID) ivPlayerColor.setBackgroundColor(Color.parseColor(it.Color))
                    }
                    tvPoints.append("0")
                }
            }
        }




        bEnd.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                networkFunctions.gameLeave(gameID)
                runOnUiThread {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        bSteps.setOnClickListener {
            popupActivities.gameSteps(gameSteps)
        }

        bYes.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch { networkFunctions.letPlayerIn(userWantToJoin) }
            joinLayout.visibility = View.INVISIBLE
        }
        bNo.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch { networkFunctions.notLetPlayerIn(userWantToJoin) }
            joinLayout.visibility = View.INVISIBLE
        }
        tvStart.setOnClickListener {
            a++
            if (a == 7) tvStart.visibility = View.INVISIBLE
        }

        tvGameID.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("text", gameID)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Текст скопирован: $gameID", Toast.LENGTH_SHORT).show()
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        CoroutineScope(Dispatchers.IO).launch {
            networkFunctions.gameLeave(gameID)
        }
    }


    // что бы походить надо отправит на бэк MovPiece
    private fun handleWebSocketMessage(message: String) {
        // Логика обработки сообщения
        Log.d("ChessActivity", "Received message: $message")
        // Пример: обновление UI на основе сообщения
        runOnUiThread {
            when (message.split("MessageType\":\"")[1].split("\",")[0]) {
                "JoinResult" -> {
                    val response = Gson().fromJson(message, ResultJoinTheGame::class.java)
                    println(response)
                    CoroutineScope(Dispatchers.IO).launch {
                        val gameField = networkFunctions.getPlayingField(gameID)
                        if (gameField.Board != null) {
                            numColumnsGame = gameField.Board.size
                            runOnUiThread {
                                tvGameID.text = gameID
                                tvGameName.text = gameField.GameName
                                rvGameFigures.layoutManager = GridLayoutManager(this@ChessActivity, gameField.Board.size)
                                gameBoard = gameField.Board
                                rvGameFigures.adapter = RVFigureAdapter(this@ChessActivity, gameBoard)

                                // gridFight
                                val fightArena = baseFunctionsChess.createCommonChessBattleArena()
                                gridFight.numColumns = numColumnsGame
                                val fightAdapter = AdapterFightArea(this@ChessActivity, fightArena)
                                gridFight.adapter = fightAdapter

                                rvPlayersInfo.layoutManager = LinearLayoutManager(applicationContext)
                                playersList = gameField.Players
                                rvPlayersInfo.adapter = RVPlayerInfoAdapter(this@ChessActivity, gameField.Players)
                                gameField.Players.forEach {
                                    if (it.PlayerId == userID) ivPlayerColor.setBackgroundColor(Color.parseColor(it.Color))
                                }
                                tvPoints.append("0")
                            }
                        }
                    }
                }

                "Join" -> {
                    val response = Gson().fromJson(message, JoinTheGame::class.java)
                    println(response)
                    joinLayout.visibility = View.VISIBLE
                    userWantToJoin = response.PersonId
                    //JoinTheGame(Message=Р—Р°СЏРІРєР° РЅР° РІСЃС‚СѓРїР»РµРЅРёРµ РІ РёРіСЂСѓ, StatusCode=200, Success=true, MessageType=Join, Nickname=mysupertwink2, PersonId=a8636b46-a206-4121-b502-02da3536aba4)

                }

                "ReverseTimer" -> {
                    val response = Gson().fromJson(message, ReverseTimer::class.java)
                    var time = response.Time
                    println(response)

                    tvStart.text = response.Message
                    if (response.Message.contains("1")) tvStart.visibility = View.GONE

                }

                "TimerGame" -> {
                    val response = Gson().fromJson(message, DurationGame::class.java)
                    println(response)
                    tvTime.text = response.Time
                }

                "PersonTime" -> {
                    val response = Gson().fromJson(message, RemainingTimePerson::class.java)
                    println(response)
                    if (!isInitialize) try { playersList; isInitialize = true } catch (e: Exception){}
                    playersList.forEachIndexed { j, it ->
                        println("pID - ${it.PlayerId}")
                        println("ppID - ${response.PersonId}")

                        if (response.PersonId == userID){
                            var show2 = true
                            if (show1) {
                                if (show2) {
                                    show2 = false
                                    show1 = false
                                    functions.showSnackBar(tvTime, "Сейчас ваш ход", getColor(R.color.light_green), Snackbar.LENGTH_SHORT, false)
                                }
                            }
                        } else show1 = true

                        if (it.PlayerId == response.PersonId) {
                            playersList[j].Time = response.Time
                            playersList[j].Color = "#2DF600"
                            rvPlayersInfo.adapter?.notifyItemChanged(j)
                        }
                        else {
                            if (playersList[j].Color != "#B8B5B5") {
                                playersList[j].Color = "#B8B5B5"
                                rvPlayersInfo.adapter?.notifyItemChanged(j)
                            }
                        }
                    }

                }

                "AddPlayer" -> {
                    val response = Gson().fromJson(message, AddPerson::class.java)
                    println(response)
                    val player = Player(response.PersonId, response.Nickname, response.Time, "#B8B5B5")
                    playersList.add(player)
                    rvPlayersInfo.adapter?.notifyItemInserted(playersList.size-1)
                }

                "UpdateBoard" -> {
                    // Тут появляются фигуры нового игрока
                    val gameField = Gson().fromJson(message, UpdateBoard::class.java)
                    println(gameField)
                    rvGameFigures.layoutManager = GridLayoutManager(this@ChessActivity, gameField.Board.size)
                    rvGameFigures.adapter = RVFigureAdapter(this@ChessActivity, gameField.Board)
                }

                "Finished" -> {
                    val response = Gson().fromJson(message, FinishGame::class.java)
                    println(response)
                }

                "GameOver" -> {
                    val response = Gson().fromJson(message, GameOverPlayer::class.java)
                    println(response)

                    // тут разным людям разное приходит
                }

                "PlayerIsActive" -> {
                    val response = Gson().fromJson(message, PlayerIsActive::class.java)
                    println(response)
                }

                "InActiveTime" -> {
                    // времени у неактивного игрока
                    val response = Gson().fromJson(message, ReversTimeAnActivePlayer::class.java)
                    println(response)
                }

                "RemovePlayer" -> {
                    // удаляет игрока из списка
                    val response = Gson().fromJson(message, RemovePlayer::class.java)
                    println(response)
                }

                "UpdateColor" -> {
                    // если чувак вышел - меняю цвета у игроков
                    val response = Gson().fromJson(message, UpdateColorPlayer::class.java)
                    println(response)
                    ivPlayerColor.setBackgroundColor(Color.parseColor(response.Color))
                }

                "KillPiece" -> {
                    val response = Gson().fromJson(message, KillAllPiece::class.java)
                    println(response)

                    var pawn = 0
                    var horse = 0
                    var elephant = 0
                    var castle = 0
                    var queen = 0
                    var king = 0

                    for (piece in response.KillPiece) {
                        when (piece) {
                            PieceType.Pawn -> pawn++
                            PieceType.Knight -> horse
                            PieceType.Bishop -> elephant++
                            PieceType.Rook -> castle++
                            PieceType.Queen -> queen++
                            PieceType.King -> king++
                        }

                        tvPawnKilled.text = pawn.toString()
                        tvHorseKilled.text = horse.toString()
                        tvElephantKilled.text = elephant.toString()
                        tvCastleKilled.text = castle.toString()
                        tvQueenKilled.text = queen.toString()
                        tvKingKilled.text = king.toString()
                    }
                }

                "Score" -> {
                    val response = Gson().fromJson(message, UpdateScore::class.java)
                    println(response)

                    val content = tvPoints.text.split(": ")
                    tvPoints.text = "${content[0]}: ${response.Score}"

                }

                "moving" -> {
                    val response = Gson().fromJson(message, NewMove::class.java)
                    println(response)
                    gameSteps.add(response.Move)
                }

                "StateGame" -> {
                    // информация о состоянии игры (игра на паузе не на паузе)
                    val response = Gson().fromJson(message, GameStateMessage::class.java)
                    println(response)
                }

                "PotionUse" -> {
                    val response = Gson().fromJson(message, UsePotion::class.java)
                    println(response)
                }

                "GamePlayerTheResult" -> {
                    val response = Gson().fromJson(message, ResultPlayerTheGame::class.java)
                    println(response)
//                    popupActivities.endGameView(response.Status, response.League, response.Score.PotionScore!!, response.Score.AddScoreWine!!, response.Score.TotalScore, response.Rating)
                    popupActivities.endGameView(response.Status, response.League, 245, 136, 381, response.Rating)
                }

                else -> {
                    println("Unknown message: Получено неизвестное сообщение: $message")
                }
            }
        }
    }



}