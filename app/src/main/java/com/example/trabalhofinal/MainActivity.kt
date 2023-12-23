package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        val buttonIrMP = findViewById<Button>(R.id.btnIrParaMenuprincipal)
        val signupButton = findViewById<Button>(R.id.signupButton)

        // Adicionar animação de clique apenas para o btnIrParaMenuprincipal
        buttonIrMP.setOnClickListener {
            exibirImagemTemporariamente(buttonIrMP) {
                irParaMenuprincipal()
            }
        }

        signupButton.setOnClickListener {
            // Chamar a função abrirCriarConta() diretamente para o signupButton
            abrirCriarConta()
        }
    }

    private fun exibirImagemTemporariamente(button: View, onComplete: () -> Unit) {
        val imagemExibida = findViewById<ImageView>(R.id.lapis)

        // Tornar a imagem visível
        imagemExibida.visibility = View.VISIBLE

        // Aplicar a animação de rotação
        val rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
        imagemExibida.startAnimation(rotationAnimation)

        // Aplicar a animação de clique no botão específico
        val clickAnimation = AnimationUtils.loadAnimation(this, R.anim.button_click_animation)
        clickAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                // Iniciar a animação normal no final da animação de clique
                button.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, R.anim.button_normal))
                // Esconder a imagem após 3 segundos
                Handler(Looper.getMainLooper()).postDelayed({
                    imagemExibida.clearAnimation()
                    imagemExibida.visibility = View.GONE
                    onComplete.invoke() // Chamada da função de conclusão
                }, 3000)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        button.startAnimation(clickAnimation)
    }

    // Função que faz com que vá para o menu principal da app
    private fun irParaMenuprincipal() {
        val intent1 = Intent(this@MainActivity, Menuprincipal::class.java)
        startActivity(intent1)
    }

    // Função que faz com que vá para o menu criar conta da app
    private fun abrirCriarConta() {
        val intent2 = Intent(this@MainActivity, CriarConta::class.java)
        startActivity(intent2)
    }
}
