package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CriarConta : AppCompatActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criarconta)

        val btnCriarConta = findViewById<Button>(R.id.btnCriarContaFinal)
        btnCriarConta.setOnClickListener {
            processarCriarConta()
            // Adicione a animação de clique
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
        }
    }

    fun processarCriarConta() {
        // Obter referências para os EditTexts
        val editTextNome = findViewById<EditText>(R.id.editTextNome)
        val editTextApelido = findViewById<EditText>(R.id.editTextApelido)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        // Obter os valores inseridos pelos usuários
        val nome = editTextNome.text.toString()
        val apelido = editTextApelido.text.toString()
        val username = editTextUsername.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        // Verificar se todos os campos estão preenchidos
        if (nome.isNotEmpty() && apelido.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            //aqui colocas o caralho dos dados para enviar para a merda da API

            Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
            abrirMain()
        } else {
            // Se algum campo estiver vazio, exibir uma mensagem de erro
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }
    //função que faz com que vá para o menu inicial da app(main)
    private fun abrirMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
