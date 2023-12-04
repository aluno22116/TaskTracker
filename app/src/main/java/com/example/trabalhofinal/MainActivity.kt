package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupButtonListeners()
    }
    private fun setupButtonListeners() {
        val button = findViewById<Button>(R.id.btnIrParaMenuprincipal)
        button.setOnClickListener {
            irParaMenuprincipal()
        }

        val signupButton = findViewById<Button>(R.id.signupButton)
        signupButton.setOnClickListener {
            abrirCriarConta()
        }
    }
    fun irParaMenuprincipal() {
        val intent = Intent(this@MainActivity, Menuprincipal::class.java)
        startActivity(intent)
    }
    fun abrirCriarConta() {
        val intent = Intent(this@MainActivity, CriarConta::class.java)
        startActivity(intent)
    }
}