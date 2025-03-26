package com.example.chess.Functions

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.example.chess.Activities.ChessActivity
import com.example.chess.BackendClasses.CreateGame.GameCreate
import com.example.chess.BackendClasses.CreateGame.GameJoin
import com.example.chess.BackendClasses.Game.GameData
import com.example.chess.BackendClasses.Game.MovePiece
import com.example.chess.BackendClasses.Profile.DataProfile
import com.example.chess.BackendClasses.Profile.PublicGame
import com.example.chess.BackendClasses.Profile.allPublicGames
import com.example.chess.BackendClasses.Profile.historyGames
import com.example.chess.MEDIA_TYPE
import com.example.chess.baseUrl
import com.example.chess.gameID
import com.example.chess.token
import com.example.chess.wsL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class NetworkFunctions(private val context: Context) {


//    fun connectToWS() {
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("$baseUrl/Game/connect")
//            .header("accept", "application/json")
//            .header("Authorization", "Bearer $token")
//            .build()
//
//        val listerner = WebSocketListener()
//        val ws = client.newWebSocket(request, listerner)
//    }

    fun gameCreate(name: String, playersNum: Int, private: Boolean, mode: String, prekol: Boolean) {
        val client = OkHttpClient()
        val requestBody = ""

        val request = Request.Builder()
            .url("$baseUrl/Game/create-game?name=$name&players=$playersNum&isPrivate=$private&mode=$mode&isPotion=$prekol")
            .post(requestBody.toRequestBody(MEDIA_TYPE))
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { trashResponse ->
            if (!trashResponse.isSuccessful) println("Unexpected code $trashResponse")
            val responseBody = trashResponse.body?.string()
            println("trash create game response: $responseBody")

            // Парсим JSON в объект
            val response = Gson().fromJson(responseBody, GameCreate::class.java)
            println(trashResponse)
            response.statusCode = trashResponse.toString().split("code=")[1].split(",")[0].toInt()
            when (response.statusCode) {
                200 -> {
                    println("create game response - ${response}")
                    gameID = response.gameId
                    gameJoin(gameID)
                }
                else -> {
                    Log.e("create error", "BEDA")
                    //Toast.makeText(context.applicationContext, "Ошибка создания игры", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun gameJoin(gameId: String) {
        val client = OkHttpClient()
        val requestBody = ""

        val request = Request.Builder()
            .url("$baseUrl/Game/login-game?gameId=$gameId")
            .post(requestBody.toRequestBody(MEDIA_TYPE))
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { trashResponse ->
            if (!trashResponse.isSuccessful) println("Unexpected join code $trashResponse")
            val responseBody = trashResponse.body?.string()
            println("trash join response: $trashResponse")

            // Парсим JSON в объект
            val response = Gson().fromJson(responseBody, GameJoin::class.java)
//            response.statusCode = trashResponse.toString().split("code=")[1].split(",")[0].toInt()
            when (response.statusCode) {
                200 -> {
                    println("join response - ${response}")
                    gameID = gameId
                    val intent = Intent(context.applicationContext, ChessActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                400 -> {
                    println("need to connect to WS")
                    wsL.connectToWS()
                    //Toast.makeText(context.applicationContext, "Вы не подключены к серверу", Toast.LENGTH_SHORT).show()
                }
                404 -> {
                    println("game isn`t real")
                    //Toast.makeText(context.applicationContext, "Игра не существует", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Log.e("join error", "BEDA")
                    //Toast.makeText(context.applicationContext, "Error 3: JOIN_ERROR", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun gameLeave(gameId: String) {
        val client = OkHttpClient()
        val requestBody = ""

        val request = Request.Builder()
            .url("$baseUrl/Game/leave-game?gameId=$gameId")
            .post(requestBody.toRequestBody(MEDIA_TYPE))
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { trashResponse ->
            if (!trashResponse.isSuccessful) println("Unexpected code $trashResponse")
            val responseBody = trashResponse.body?.string()
            println("trash leave response: $responseBody")
        }
    }

    fun letPlayerIn(userWantToJoin: String): Boolean {
        val client = OkHttpClient()
        val requestBody = ""

        val request = Request.Builder()
            .url("$baseUrl/Game/approve-player?gameId=$gameID&personId=$userWantToJoin")
            .post(requestBody.toRequestBody(MEDIA_TYPE))
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .header("content-type", "application/x-www-form-urlencoded")
            .build()

        client.newCall(request).execute().use { response ->
            println("let player response $response")
            return response.isSuccessful
        }
    }

    fun notLetPlayerIn(userWantToJoin: String): Boolean {
        val client = OkHttpClient()
        val requestBody = ""

        val request = Request.Builder()
            .url("$baseUrl/Game/approve-player?gameId=$gameID&personId=$userWantToJoin")
            .post(requestBody.toRequestBody(MEDIA_TYPE))
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .header("content-type", "application/x-www-form-urlencoded")
            .build()

        client.newCall(request).execute().use { response ->
            println("let player response $response")
            return response.isSuccessful
        }
    }

    fun getPlayingField(gameID: String): GameData {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("$baseUrl/Game/playing-field?gameId=$gameID")
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { trashResponse ->
            if (!trashResponse.isSuccessful) println("Unexpected code $trashResponse")
            val responseBody = trashResponse.body?.string()
            println("trash playing field response string: $responseBody")
            val response = Gson().fromJson(responseBody, GameData::class.java)
            println("playing field response string: $response")
            return response
        }

    }

    fun getRandomGame() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("$baseUrl/Game/game")
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { trashResponse ->

            if (trashResponse.isSuccessful){
                gameID = trashResponse.body?.string()!!.replace("\"", "")
                gameJoin(gameID)
            }
            else {
                println("Игра не найдена")
                //Toast.makeText(context.applicationContext, "Игра не найдена", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getAllPublicGames(): allPublicGames? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("$baseUrl/Game/games")
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { trashResponse ->
            if (!trashResponse.isSuccessful) println("Unexpected code $trashResponse")
            val responseBody = trashResponse.body?.string()
            println("trash available games response string: $responseBody")
            try {
                val type = object : TypeToken<allPublicGames>() {}.type
                val response = Gson().fromJson<allPublicGames>(responseBody, type)
                println("available games response string: $response")
                return response
            }catch (e: Exception) {
                return mutableListOf(PublicGame("Нет игр", "-1", 0, 0, " ", false))
            }

        }
    }

    fun getHistoryGames(): historyGames? {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("$baseUrl/Profile/games")
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { trashResponse ->
            if (!trashResponse.isSuccessful) println("Unexpected code $trashResponse")
            val responseBody = trashResponse.body?.string()
            println("trash available games response string: $responseBody")
            val type = object : TypeToken<historyGames>() {}.type
            val response = Gson().fromJson<historyGames>(responseBody, type)
            println("available games response string: $response")
            return response
        }
    }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        val client = OkHttpClient()
        val MEDIA_TYPE = "application/json".toMediaType()

        val requestBody = "{\n  \"$oldPassword\": \"stringstri\",\n  \"$newPassword\": \"stringstri\"\n}"

        val request = Request.Builder()
            .url("$baseUrl/Auth/password")
            .put(requestBody.toRequestBody(MEDIA_TYPE))
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .build()

        client.newCall(request).execute().use { response ->
            println("change password response $response")
            return response.isSuccessful
        }
    }

    fun accountDelete(): Boolean {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("$baseUrl/Profile/account")
            .delete("".toRequestBody())
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        client.newCall(request).execute().use { response ->
            println("account delete response $response")

            return response.isSuccessful
        }
    }

    fun getPlayerScore(): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$baseUrl/Profile/get-score")
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                return response.body!!.string()
            }
            else return "error"
        }
    }

    fun getPlayerData(): DataProfile {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$baseUrl/Profile/get-player-data")
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()
        client.newCall(request).execute().use { trashResponse ->
            if (!trashResponse.isSuccessful) println("Unexpected join code $trashResponse")
            val responseBody = trashResponse.body?.string()
            println("trash join response: $trashResponse")
            return Gson().fromJson(responseBody, DataProfile::class.java)
        }
    }

    private fun isNetworkAvailable(): Boolean { // Добавьте проверку состояния сети перед переподключением.
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun sendMessageToServer(message: MovePiece) {
        // Отправка сообщения через WebSocket
        wsL.sendMessage(Gson().toJson(message))
        println("Message send - $message")

    }

}