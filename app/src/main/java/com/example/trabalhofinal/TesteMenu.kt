package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
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

    private fun handleMenuItemClick(itemId: Int): Boolean {
         when (itemId) {
            R.id.menu -> Intent(this, Menuprincipal::class.java)
            R.id.perfil -> Intent(this, Perfil::class.java)
            R.id.notas -> Intent(this, Bnotas::class.java)
            R.id.tarefas -> Intent(this, Tarefas::class.java)
            R.id.definicoes -> Intent(this, Menuprincipal::class.java)
            R.id.calendario -> Intent(this, Calendario::class.java)
            else -> null
        }

        intent?.let {
            startActivity(it)
            // Feche o DrawerLayout após a seleção do menu
            drawerLayout.closeDrawer(GravityCompat.START)
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

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    fun openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}
