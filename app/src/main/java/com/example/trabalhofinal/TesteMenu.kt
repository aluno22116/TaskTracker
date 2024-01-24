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
import androidx.fragment.app.Fragment
import com.example.trabalhofinal.databinding.ActivityTestemenuBinding
import com.google.android.material.navigation.NavigationView

open class TesteMenu : AppCompatActivity() {
    public lateinit var drawerLayout: DrawerLayout
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

    fun handleMenuItemClick(itemId: Int): Boolean {
        when (itemId) {
            R.id.menu -> {
                val intent = Intent(this, Menuprincipal::class.java)
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.END)
                finish()
                return true
            }
            R.id.vamosver -> {
                val fragment = vamosver()
                abrirFragmento(fragment)
                return true
            }
            R.id.perfil -> {
                val fragment = Perfil()
                abrirFragmento(fragment)
                return true
            }
            else -> return false
        }
    }

    private fun abrirFragmento(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START, true);
        Log.e("ERRO","ta a fechar a puta do drawer")
        // Fecha o DrawerLayout após um pequeno atraso para dar tempo à animação

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