package com.example.trabalhofinal

import Perfil
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.trabalhofinal.databinding.ActivityTestemenuBinding
import com.google.android.material.navigation.NavigationView

open class TesteMenu : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityTestemenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestemenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem.itemId)
        }


    }

    public fun handleMenuItemClick(itemId: Int): Boolean {
        val intent = when (itemId) {
            R.id.menu -> Intent(this, Menuprincipal::class.java)
            R.id.notas -> Intent(this, Bnotas::class.java)
            R.id.vamosver-> Intent(this, vamosver::class.java)
            R.id.perfil-> Intent(this, Perfil::class.java)

            else -> null
        }

        intent?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Adiciona a flag para limpar as atividades no topo da pilha
            startActivity(it)
            drawerLayout.closeDrawer(GravityCompat.START)
            finish() // Encerra a atividade atual
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    fun clicarAbrirDrawer() {
        Log.d("MeuApp", "Clicou no botão Abrir Drawer")

        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    }

