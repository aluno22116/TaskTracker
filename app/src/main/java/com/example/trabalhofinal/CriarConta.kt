package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinal.model.User
import com.example.trabalhofinal.model.UserRequest
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    private fun processarCriarConta() {
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
            // Verificar o formato do e-mail
            if (validaEmail(email)) {
                // Continuar com o processamento
                val utilizador = User(null, nome, apelido, username, email, password)
                val user = UserRequest(utilizador)
                val call = RetrofitInitializer().service().addUser("Bearer Tostas",user)

                call.enqueue(object : Callback<UserRequest?> {
                    override fun onResponse(call: Call<UserRequest?>, response: Response<UserRequest?>) {
                        if (response.isSuccessful) {
                            // A requisição foi bem-sucedida, processar a resposta se necessário
                            val userResponse = response.body()
                            if (response.code() == 200) {
                                if (userResponse != null) {
                                    Log.e("Novo usuário adicionado", userResponse.user.toString())
                                }
                            }
                        } else {
                            // A requisição falhou, verificar o código de resposta e tratar conforme necessário
                            Log.e("Falha na adição de usuário", "Código de resposta: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<UserRequest?>, t: Throwable) {
                        // Ocorreu uma falha na chamada à API, tratar conforme necessário
                        t.printStackTrace()
                        t.message?.let { Log.e("Falha na Chamada à API", it) }
                    }
                })

                Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                abrirMain()
            } else {
                // Exiba uma mensagem de erro informando que o formato do e-mail é inválido
                Toast.makeText(this, "Formato de e-mail inválido", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Se algum campo estiver vazio, exibir uma mensagem de erro
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }

    // Função que faz com que vá para o menu inicial da app (main)
    private fun abrirMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun validaEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
}
