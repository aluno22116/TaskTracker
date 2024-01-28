package ipt.dam2324.tasktracker

import BlocoNotas
import Perfil
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import ipt.dam2324.tasktracker.databinding.ActivityTestemenuBinding
import ipt.dam2324.tasktracker.model.NoteResponse
import ipt.dam2324.tasktracker.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

 open class TesteMenu : AppCompatActivity() {
     private lateinit var drawerLayout: DrawerLayout
     private lateinit var navigationView: NavigationView
     private lateinit var drawerToggle: ActionBarDrawerToggle
     private lateinit var binding: ActivityTestemenuBinding

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_testemenu) // Substitua com o layout da sua atividade


         drawerLayout = findViewById(R.id.drawer_layout)
         navigationView = findViewById(R.id.nav_view)
         drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
         drawerLayout.addDrawerListener(drawerToggle)
         drawerToggle.syncState()
         supportActionBar?.setDisplayHomeAsUpEnabled(true)
         navigationView.setNavigationItemSelectedListener { menuItem ->
             handleMenuItemClick(menuItem.itemId)
         }

     }

    //Função que trata os cliques nos itens do menu.
     public fun handleMenuItemClick(itemId: Int): Boolean {
         when (itemId) {
             R.id.menu -> {
                 abrirMenu()
                 Handler().postDelayed({
                     finish()
                 }, 500)

                 return true
             }

             R.id.vamosver -> {
                 val fragment = vamosver()
                 abrirFragmento(fragment)
                 return true
             }

             R.id.perfil -> {
                 val fragment = Perfil()
                 abrirFragmento(fragment)
                 return true
             }

             R.id.notas -> {
                 val userId = getSavedUserId()
                 getNotes(userId)
                 return true
             }

             R.id.sobre -> {
                 val fragment = Sobre()
                 abrirFragmento(fragment)
                 return true
             }

             R.id.btnUtilizadoress -> {
                 val fragment = Utilizadores()
                 abrirFragmento(fragment)
                 return true
             }

             R.id.btnFechar -> {

                 Handler().postDelayed({
                     finish()
                     // Encerra a Menuprincipal
                     val intentMenuprincipal = Intent(this, MainActivity::class.java)

                     intentMenuprincipal.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                     startActivity(intentMenuprincipal)

                     // Encerra a atividade atual (TesteMenu)


                 }, 500)
                 return true
             }
             else -> return false
         }
     }
    //Função que abre um fragmento na atividade.
    private fun abrirFragmento(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        drawerLayout.closeDrawer(GravityCompat.END, true);
        Log.e("ERRO","ta a fechar a puta do drawer")
        }

     //Função que abre a Menuprincipal.
     private fun abrirMenu(){
         val intent = Intent(this, Menuprincipal::class.java)
         startActivity(intent)
         drawerLayout.closeDrawer(GravityCompat.END)
     }

    //Função que obtém as notas do utilizador da API e salva na SharedPreferences.
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
                                val fragment = BlocoNotas()
                                abrirFragmento(fragment)

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
    //Função que salva as notas do utilizador na SharedPreferences.
    private fun saveNotesToSharedPreferences(notesString: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("notes", notesString)
        editor.apply()
    }

    //Função que obtém o ID do utilizador salvo na SharedPreferences.
    private fun getSavedUserId(): String {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }

     //Sobrescreve o método onOptionsItemSelected para lidar com eventos de clique nos itens do menu.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    }

