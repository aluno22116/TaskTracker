package com.example.trabalhofinal


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

        // Configurar o NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem.itemId)
        }
    }

    private fun setupButtonListeners() {
        val buttonCriadores = findViewById<Button>(R.id.vamosver)
        val buttonPerfil = findViewById<Button>(R.id.perfil)
        //val buttonBNotas = findViewById<Button>(R.id.buttonBNotas)

        buttonCriadores.setOnClickListener {
            //it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirCriadores(vamosver())
         }

        buttonPerfil.setOnClickListener {
            //it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirPerfil(perfil())
        }
       /* buttonBNotas.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirBnotas(Bnotas())
        }*/
    }

    private fun abrirCriadores(fragment: vamosver) {

            val balanceViewFragment = vamosver()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, balanceViewFragment)
                .commit()

    }

    private fun abrirPerfil(fragment: perfil) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


   /* private fun abrirBnotas(fragment: Bnotas) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmento, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }*/

}
