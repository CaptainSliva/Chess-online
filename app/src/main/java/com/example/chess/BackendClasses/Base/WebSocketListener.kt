package com.example.chess.BackendClasses.Base

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.chess.Functions.NetworkFunctions
import com.example.chess.baseUrl
import com.example.chess.token
import com.example.chess.wssNumberConnectFailure
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener: WebSocketListener() {

    private lateinit var webSocket: WebSocket
    private val subscribers = mutableListOf<(String) -> Unit>()
    private var context: Context? = null

    fun initialize(context: Context) {
        this.context = context
        connectToWS()
    }

    fun connectToWS() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$baseUrl/Game/connect")
            .header("accept", "application/json")
            .header("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, this)
    }

    fun subscribe(callback: (String) -> Unit) {
        subscribers.add(callback)
    }

    fun unsubscribe(callback: (String) -> Unit) {
        subscribers.remove(callback)
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.i("ws", "WebSocket connection opened")
        wssNumberConnectFailure = 0
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("ws receive", "Received: $text")
        notifySubscribers(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d("ws", "Closing: $code $reason")
        webSocket.close(NORMAL_CLOSE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.e("ws", "Error: ${t.message}")
        Log.e("ws", "Response: ${response?.toString()}")
//        NetworkFunctions(context!!).connectToWS()
        Handler(Looper.getMainLooper()).postDelayed({
            connectToWS()
            wssNumberConnectFailure++
        }, 3000)
    }

    private fun notifySubscribers(message: String) {
        subscribers.forEach { it(message) }
    }

    companion object {
        private const val NORMAL_CLOSE_STATUS = 1000
    }


}