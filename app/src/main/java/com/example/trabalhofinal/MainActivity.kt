package com.example.trabalhofinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinal.model.TokenJWT
import com.example.trabalhofinal.model.User
import com.example.trabalhofinal.model.UserRequest
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var tokenJWT:String
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

    //FUNÇAO QUE REGISTA UM UTILIZADOR (ADAPTAR AO REGISTO)
    private fun addUserApi() {
        val utilizador =  User(null,"teste1", "teste1", "teste1", "teste1@teste.com", "teste123");
        val user = UserRequest(utilizador);

        Log.e("Dados da Requisição", utilizador.toString())
        val call = RetrofitInitializer().service().addUser("Bearer "+tokenJWT,user)

        call.enqueue(object : Callback<UserRequest?> {
            override fun onResponse(call: Call<UserRequest?>, response: Response<UserRequest?>) {
                if (response.isSuccessful) {
                    // A requisição foi bem-sucedida, processar a resposta se necessário
                    val userResponse = response.body()
                    if (response.code()==200) {
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
    }


    //FUNCOES DE LOGIN (AINDA POR ADAPTAR)
    private fun loginJWT(onTokenReceived: () -> Unit) {
        val call = RetrofitInitializer().service().loginJWT("admin","admin")
        call.enqueue(
            object : Callback<TokenJWT> {
                override fun onFailure(call: Call<TokenJWT>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("Erro","Erro no login")
                }
                override fun onResponse(call: Call<TokenJWT>, response: Response<TokenJWT>) {

                    tokenJWT = response.body()?.token.toString()
                    Log.i("INFO",tokenJWT)
                    onTokenReceived()
                }
            }
        )
    }

    private fun loginJWT(username: String, password: String,  onResult: (TokenJWT?) -> Unit){
        val call = RetrofitInitializer().service().loginJWT(username, password)
        call.enqueue(
            object : Callback<TokenJWT> {
                override fun onFailure(call: Call<TokenJWT>, t: Throwable) {
                    t.printStackTrace()
                    onResult(null)
                }
                override fun onResponse(call: Call<TokenJWT>, response: Response<TokenJWT>) {
                    val tokenResult = response.body()
                    onResult(tokenResult)
                }
            }
        )
    }
}
