package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var splashText: TextView
    private val textToDisplay = "TaskTracker"
    private var currentIndex = 0

    // Ajuste o delayMillis e durationMillis conforme necessário
    private val delayMillis: Long = 100 // Tempo de atraso entre cada letra em milissegundos
    private val durationMillis: Long = 1000// Duração total da animação em milissegundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashText = findViewById(R.id.splashText)

        Handler(Looper.getMainLooper()).postDelayed({
            animateText()
        }, delayMillis)
    }

    private fun animateText() {
        runOnUiThread {
            if (currentIndex < textToDisplay.length) {
                splashText.append(textToDisplay[currentIndex].toString())
                currentIndex++

                Handler(Looper.getMainLooper()).postDelayed({
                    animateText()
                }, delayMillis)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    startNextActivity()
                }, durationMillis - currentIndex * delayMillis) // Ajuste do tempo total com base no número de letras
            }
        }
    }

    private fun startNextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
