package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import com.example.trabalhofinal.databinding.ActivityMenuprincipalBinding
import com.google.android.material.navigation.NavigationView

class Menuprincipal : TesteMenu() {

    private lateinit var binding: ActivityMenuprincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use o layout inflater apropriado para a atividade Menuprincipal
        binding = ActivityMenuprincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()

        // Configurar o NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem.itemId)
        }
    }

    private fun setupButtonListeners() {
        val buttonTarefas = findViewById<Button>(R.id.buttonTarefas)
        val buttonPerfil = findViewById<Button>(R.id.buttonPerfil)
        val buttonBNotas = findViewById<Button>(R.id.buttonBNotas)
        val buttonCalendario = findViewById<Button>(R.id.buttonCalendario)

        buttonTarefas.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirTarefas()
        }
        buttonPerfil.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirPerfil()
        }
        buttonBNotas.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirBNotas()
        }
        buttonCalendario.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirCalendario()
        }
    }

    private fun abrirCalendario() {
        val intent = Intent(this, Calendario::class.java)
        startActivity(intent)
    }

    private fun abrirPerfil() {
        val intent = Intent(this, Perfil::class.java)
        startActivity(intent)
    }

    private fun abrirBNotas() {
        val intent = Intent(this, Bnotas::class.java)
        startActivity(intent)
    }

    private fun abrirTarefas() {
        val intent = Intent(this, Tarefas::class.java)
        startActivity(intent)
    }

    // Adicione outras funções necessárias
}
