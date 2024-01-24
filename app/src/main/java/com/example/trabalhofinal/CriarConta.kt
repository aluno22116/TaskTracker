package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinal.model.Note
import com.example.trabalhofinal.model.NoteRequest
import com.example.trabalhofinal.model.User
import com.example.trabalhofinal.model.UserRequest
import com.example.trabalhofinal.model.UserResponse
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
        val editTextNome = findViewById<EditText>(R.id.editTextNome)
        val editTextApelido = findViewById<EditText>(R.id.editTextApelido)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        val nome = editTextNome.text.toString()
        val apelido = editTextApelido.text.toString()
        val username = editTextUsername.text.toString()
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (nome.isNotEmpty() && apelido.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            if (validaEmail(email)) {
                val utilizador = User(null, nome, apelido, username, email, password)
                val notas = Note(null, username,null,null)
                val user = UserRequest(utilizador)
                val notes = NoteRequest(notas)


                //userNotes(username, not1es, user)
                // Verifica se o username já existe antes de tentar criar o usuário
                getUsersApi(username, notes, user)
            } else {
                Toast.makeText(this, "Formato de e-mail inválido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUsersApi(username: String, note: NoteRequest, user: UserRequest) {
        val call = RetrofitInitializer().service().getUsers("Bearer Tostas")
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()?.users
                    if (users != null) {
                        val matchedUser = users.find { it.username == username }
                        if (matchedUser != null) {
                            // Usuário já existe, exibe uma mensagem de erro
                            Toast.makeText(this@CriarConta, "Username já existente. Insira outro username", Toast.LENGTH_SHORT).show()
                        } else {
                            userNotes(note, user)
                            // Usuário não existe, prossegue com o registo
                           // realizarRegisto(user)
                        }
                    } else {
                        Log.e("Erro", "Lista de usuários nula.")
                    }
                } else {
                    Log.e("Erro", "Erro na chamada à API: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("Erro", "Erro na chamada à API: ${t.message}")
            }
        })
    }

    private fun userNotes(note: NoteRequest, user: UserRequest) {
        // API call to add user notes
        val notesCall = RetrofitInitializer().service().createNoteSpace("Bearer Tostas", note)
        notesCall.enqueue(object : Callback<NoteRequest> {
            override fun onResponse(call: Call<NoteRequest>, response: Response<NoteRequest>) {
                if (response.isSuccessful) {
                    // Continue with the registration process or any other logic
                    // Now, make another API call to add the user to a different worksheet
                    realizarRegisto(user)
                } else {
                    Log.e("Erro", "Erro na chamada à API de notas: ${response.message()}")
                    // Handle the error as needed
                }
            }

            override fun onFailure(call: Call<NoteRequest>, t: Throwable) {
                t.printStackTrace()
                Log.e("Erro", "Erro na chamada à API de notas: ${t.message}")
                // Handle the error as needed
            }
        })
    }




    private fun realizarRegisto(user: UserRequest) {
        val call = RetrofitInitializer().service().addUser("Bearer Tostas", user)
        call.enqueue(object : Callback<UserRequest?> {
            override fun onResponse(call: Call<UserRequest?>, response: Response<UserRequest?>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (response.code() == 200) {
                        if (userResponse != null) {
                            Log.e("Novo usuário adicionado", userResponse.user.toString())
                            Toast.makeText(this@CriarConta, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                            abrirMain()
                        }
                    }
                } else {
                    Log.e("Falha na adição de usuário", "Código de resposta: ${response.code()}")
                    Toast.makeText(this@CriarConta, "Falha ao criar conta. Tente novamente.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserRequest?>, t: Throwable) {
                t.printStackTrace()
                Log.e("Falha na Chamada à API", t.message ?: "Erro desconhecido")
                Toast.makeText(this@CriarConta, "Falha ao criar conta. Tente novamente.", Toast.LENGTH_SHORT).show()
            }
        })
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
