package com.example.trabalhofinal

import BlocoNotas
import Perfil
import android.os.Bundle
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
        //abrirDrawerBtn()
        // Configurar o NavigationView
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
            //it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirCriadores(vamosver())
         }

        buttonPerfil.setOnClickListener {
            //it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirPerfil(Perfil())
        }
        buttonBNotas.setOnClickListener {
          //  it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirBnotas(BlocoNotas())
        }
    }

    private fun abrirCriadores(fragment: vamosver) {

            val balanceViewFragment = vamosver()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, balanceViewFragment)
                .commit()

    }

    private fun abrirPerfil(fragment: Perfil) {
        val balanceViewFragment = Perfil()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }


    private fun abrirBnotas(fragment: BlocoNotas) {
        val balanceViewFragment = BlocoNotas()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }

   /* private fun abrirDrawerBtn(){
    val btnMenuDrawer = findViewById<ImageButton>(R.id.btnMenuDrawer)
        btnMenuDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START,true)
        }
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
    }*/

}
