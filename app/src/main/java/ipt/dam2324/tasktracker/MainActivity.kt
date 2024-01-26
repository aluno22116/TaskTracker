package ipt.dam2324.tasktracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import ipt.dam2324.tasktracker.model.UserResponse
import ipt.dam2324.tasktracker.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                loginJWT()
                // Limpar os campos de input após o clique
                limparCamposInput()
            }
        }

        signupButton.setOnClickListener {
            // Chamar a função abrirCriarConta() diretamente para o signupButton
            abrirCriarConta()
            // Limpar os campos de input após o clique
            limparCamposInput()

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
                button.startAnimation(AnimationUtils.loadAnimation(this@MainActivity,
                    R.anim.button_normal
                ))
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


    // Função que faz com que vá para o menu criar conta da app
    private fun abrirCriarConta() {
        val intent2 = Intent(this@MainActivity, CriarConta::class.java)
        startActivity(intent2)
    }

    private fun loginJWT() {
        val editTextUsername = findViewById<EditText>(R.id.usernameEditText)
        val editTextPassword = findViewById<EditText>(R.id.passwordEditText)

        if (editTextUsername != null && editTextPassword != null) {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                getUsersApi(username, password)
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("Erro", "EditTexts não inicializados corretamente.")
        }
    }

    private fun getUsersApi(username: String, password: String) {
        val call = RetrofitInitializer().service().getUsers("Bearer Tostas")
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()?.users
                    if (users != null) {
                        // Verificar se existe um usuário na lista com as credenciais fornecidas
                        val matchedUser =
                            users.find { it.username == username && it.password == password }
                        if (matchedUser != null) {

                            if (matchedUser != null) {
                                val userId = matchedUser.id.toString()
                                val nome = matchedUser.nome.toString()
                                val username = matchedUser.username.toString()
                                val email = matchedUser.email.toString()

                                // Salvar userId no SharedPreferences
                                saveUserIdToSharedPreferences(userId, nome, username, email)
                                // Usuário encontrado, faça o que precisa aqui
                                Log.i("INFO", "Usuário encontrado: $matchedUser")
                                val intent1 = Intent(this@MainActivity, Menuprincipal::class.java)
                                //  val intent2 = Intent(this@MainActivity, CriarNotas::class.java)
                                //  intent2.putExtra("userId", userId)
                                startActivity(intent1)
                                //  startActivity(intent2)
                            } else {
                                // Usuário não encontrado
                                Toast.makeText(
                                    this@MainActivity,
                                    "Usuário não encontrado na lista.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {

                            Log.e("Erro", "Lista de usuários  nula.")
                        }
                    } else {
                        Log.e("Erro", "Erro na chamada à API: ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("Erro", "Erro na chamada à API: ${t.message}")
            }
        })
    }
    private fun saveUserIdToSharedPreferences(userId: String, nome: String, username: String, email: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.putString("name", nome)
        editor.putString("username", username)
        editor.putString("email", email)
        editor.apply()

    }
    private fun limparCamposInput() {
        val editTextUsername = findViewById<EditText>(R.id.usernameEditText)
        val editTextPassword = findViewById<EditText>(R.id.passwordEditText)

        editTextUsername.text.clear()
        editTextPassword.text.clear()
    }
}