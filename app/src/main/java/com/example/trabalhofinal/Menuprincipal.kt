package com.example.trabalhofinal
import BlocoNotas
import Perfil
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.trabalhofinal.databinding.ActivityMenuprincipalBinding
import com.google.android.material.navigation.NavigationView

class Menuprincipal : TesteMenu() {

    private lateinit var binding: ActivityMenuprincipalBinding
    private lateinit var drawerLayout: DrawerLayout  // Adicione essa linha para definir o DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuprincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()
        abrirDrawerBtn()
        setStatusBarColor()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem.itemId)
        }


    }

    private fun setupButtonListeners() {
        val buttonCriadores = findViewById<Button>(R.id.vamosver)
        val buttonPerfil = findViewById<Button>(R.id.perfil)
        val buttonBNotas = findViewById<Button>(R.id.notas)

        buttonCriadores.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirCriadores(vamosver())  // Remova os parênteses se vamosver é uma classe ou objeto
        }

        buttonPerfil.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))

            abrirPerfil(Perfil())  // Remova os parênteses se Perfil é uma classe ou objeto
        }

        buttonBNotas.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirBnotas(BlocoNotas())// Remova os parênteses se BlocoNotas é uma classe ou objeto
        }
    }

    private fun abrirCriadores(fragment: vamosver) {
        val balanceViewFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }

    private fun abrirPerfil(fragment: Perfil) {
        val balanceViewFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }

    private fun abrirBnotas(fragment: BlocoNotas) {
        val balanceViewFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()

    }

    private fun abrirDrawerBtn() {
        drawerLayout = findViewById(R.id.drawer_layout)  // Substitua "seu_id_do_drawer_layout" pelo ID do seu DrawerLayout

        val btnMenuDrawer = findViewById<ImageButton>(R.id.btnMenuDrawer)
        btnMenuDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START, true)
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)  // Adicione essa linha para configurar o listener
        toggle.syncState()  // Adicione essa linha para sincronizar o estado do toggle
    }

    private fun setStatusBarColor() {
        // Verifica se a versão do Android é Lollipop ou superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Define a cor desejada da barra de status
            window.statusBarColor = resources.getColor(R.color.preto, null)
        }
    }
}
