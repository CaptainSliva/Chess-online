package com.example.chess

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chess.Activities.MainActivity
import com.example.chess.Adapters.RVArhiveAdapter
import com.example.chess.Functions.NetworkFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryGamesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history_games)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val rvArhive = findViewById<RecyclerView>(R.id.archive)
        val ibBack = findViewById<ImageButton>(R.id.ib_back)
        val networkFunctions = NetworkFunctions(this)

        CoroutineScope(Dispatchers.IO).launch {
            val historyGames = networkFunctions.getHistoryGames()
                runOnUiThread {
                    rvArhive.layoutManager = LinearLayoutManager(applicationContext)
                    rvArhive.adapter = historyGames?.let { RVArhiveAdapter(applicationContext, it) }
                }
        }

        ibBack.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}