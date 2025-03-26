package com.example.chess.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chess.BackendClasses.Login.RegAuth
import com.example.chess.Datastore.FunctionsDS
import com.example.chess.Functions.FunctionsApp
import com.example.chess.R
import com.example.chess.baseUrl
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class AutorizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_autorization)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_passwd)
        val tvHaventAcc = findViewById<TextView>(R.id.tv_havent_acc)
        val bAutorize = findViewById<Button>(R.id.b_autorize)

        val functions = FunctionsApp(this)

        tvHaventAcc.setOnClickListener {
            val intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
            finish()

        }

        bAutorize.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val client = OkHttpClient()
                val MEDIA_TYPE = "application/json".toMediaType()
                val requestBody = """
                {
                    "email": "${email.text}",
                    "password": "${password.text}"
                }
                """.trimIndent()

                val request = Request.Builder()
                    .url("$baseUrl/Auth/authorization")
                    .post(requestBody.toRequestBody(MEDIA_TYPE))
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build()

                client.newCall(request).execute().use { response ->
                    println("response $response")

                    // Извлекаем тело ответа как строку
                    val responseBody = response.body?.string()
                    println("Raw response: $responseBody")

                    // Парсим JSON в объект
                    val registrationResponse = Gson().fromJson(responseBody, RegAuth::class.java)
                    registrationResponse.statusCode = response.toString().split("code=")[1].split(",")[0].toInt()

                    when (registrationResponse.statusCode) {
                        200 -> {
                            FunctionsDS(applicationContext).saveToDataStore(registrationResponse.accessToken, registrationResponse.personId)
                            runOnUiThread {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        400 -> functions.showSnackBar(
                            bAutorize,
                            "Неправильно заполнены поля",
                            resources.getColor(R.color.light_red),
                            Snackbar.LENGTH_SHORT,
                            true,
                            "За шо"
                        )

                        403 -> functions.showSnackBar(
                            bAutorize,
                            "Логин или пароль не верен!",
                            resources.getColor(R.color.light_red),
                            Snackbar.LENGTH_SHORT,
                            true,
                            "За шо"
                        )

                        404 -> functions.showSnackBar(
                            bAutorize,
                            "Данный пользователь не найден",
                            resources.getColor(R.color.light_red),
                            Snackbar.LENGTH_SHORT,
                            true,
                            "За шо"
                        )
                    }
                }
            }
        }
    }
}