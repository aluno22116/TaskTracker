package ipt.dam2324.tasktracker

import BlocoNotas
import Perfil
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import ipt.dam2324.tasktracker.databinding.ActivityMenuprincipalBinding
import ipt.dam2324.tasktracker.model.Note
import ipt.dam2324.tasktracker.model.NoteResponse
import ipt.dam2324.tasktracker.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Menuprincipal : TesteMenu() {

    private lateinit var binding: ActivityMenuprincipalBinding
    private lateinit var drawerLayout: DrawerLayout  // Adicione essa linha para definir o DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        }

        binding = ActivityMenuprincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonListeners()
        abrirDrawerBtn()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem.itemId)
        }

    }
    //Função que configura os ouvintes de clique para os botões da interface do utilizador.
    private fun setupButtonListeners() {
        val buttonCriadores = findViewById<Button>(R.id.vamosver)
        val buttonPerfil = findViewById<Button>(R.id.perfil)
        val buttonBNotas = findViewById<Button>(R.id.notas)
        val buttonSobre = findViewById<Button>(R.id.sobre)
        val buttonUtilizadores = findViewById<Button>(R.id.Utilizadores)

        buttonCriadores.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirCriadores(vamosver())  // Remova os parênteses se vamosver é uma classe ou objeto
        }

        buttonPerfil.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))

            abrirPerfil(Perfil())  // Remova os parênteses se Perfil é uma classe ou objeto
        }
        buttonSobre.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirSobre(Sobre())  // Remova os parênteses se Perfil é uma classe ou objeto
        }

        buttonUtilizadores.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            abrirUtilizadores(Utilizadores())  // Remova os parênteses se Perfil é uma classe ou objeto
        }



        buttonBNotas.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click_animation))
            val userId = getSavedUserId()
            getNotes(userId)
           // Remova os parênteses se BlocoNotas é uma classe ou objeto
        }
    }
    //Função que abre a fragmento vamosver
    private fun abrirCriadores(fragment: vamosver) {
        val balanceViewFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }

    //Função que abre a fragmento Perfil
    private fun abrirPerfil(fragment: Perfil) {
        val balanceViewFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }
    //Função que abre a fragmento Sobre
    private fun abrirSobre(fragment: Sobre) {
        val balanceViewFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }
    //Função que abre a fragmento BlocoNotas
    private fun abrirBnotas(fragment: BlocoNotas) {
        val balanceViewFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()

    }

    private fun abrirUtilizadores(fragment: Utilizadores) {
        val balanceViewFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }

    //Função responsável por configurar e abrir o drawer (menu lateral).
    private fun abrirDrawerBtn() {
        drawerLayout = findViewById(R.id.drawer_layout)

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

    //Função responsável por obter as notas do usuário a partir da API.
    private fun getNotes(userId: String) {
       // val userId = getSavedUserId()

        // Verifica se o userId é válido
        if (userId.isNotEmpty()) {
            val call = RetrofitInitializer().service().getNotes("Bearer Tostas", userId)

            call.enqueue(object : Callback<NoteResponse> {
                override fun onResponse(call: Call<NoteResponse>, response: Response<NoteResponse>) {
                    if (response.isSuccessful) {
                        val noteResponse = response.body()
                        if (noteResponse != null) {
                            val notes = noteResponse.notes
                            val notasStrings = mutableListOf<String>()


                            if (notes != null && notes.isNotEmpty()) {
                                // O usuário tem notas, faça algo com as notas obtidas da API
                                // Por exemplo, exibir ou processar as notas
                                Log.e("Notas", "O usuário tem notas")
                                Log.i("INFO", "Usuário encontrado: $notes")

                                // Itera sobre a lista de notas e obtém o parâmetro 'notas'
                                for (note in notes) {
                                    // Verifica se o ID do usuário da nota é o mesmo que o usuário logado
                                    if (note.id.toString() == userId) {
                                        val nota = note.notas
                                        if (nota != null) {
                                            notasStrings.add(nota)
                                        }
                                    }
                                }


                                // Converte a lista de strings em uma string separada por algum caractere, por exemplo, vírgula
                                val notesString = notasStrings.joinToString()

                                // Salva a string na SharedPreferences
                                saveNotesToSharedPreferences(notesString)

                                // Inicie a atividade CriarNotas após obter as notas
                                abrirBnotas(BlocoNotas())
                            } else {
                                // O usuário não tem notas
                                Log.e("Notas", "O usuário não tem notas")
                                // Aqui você pode decidir o que fazer quando o usuário não tem notas
                                // Por exemplo, exibir uma mensagem ou realizar alguma outra ação
                            }
                        } else {
                            Log.e("Erro", "Resposta nula.")
                        }
                    } else {
                        Log.e("Erro", "Erro na chamada à API: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("Erro", "Erro na chamada à API: ${t.message}")
                }
            })
        } else {
            // userId não é válido, faça algo aqui se necessário
            Log.e("Erro", "userId inválido")
        }
    }


    // Função para converter a lista de notas em uma string
    private fun notesToString(notes: List<Note>): String {
        // Converte a lista de notas em uma string separada por algum caractere, por exemplo, vírgula
        return notes.joinToString { it.toString() }
    }

    // Função para salvar a string na SharedPreferences
    private fun saveNotesToSharedPreferences(notesString: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("notes", notesString)
        editor.apply()
    }

    //Função responsável por recuperar o identificador único do utilizador salvo nas SharedPreferences.
    private fun getSavedUserId(): String {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }
    fun esconderBotao(){
        val meuBotao = findViewById<Button>(R.id.Utilizadores)
        meuBotao.visibility = View.GONE
    }

}
