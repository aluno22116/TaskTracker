
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trabalhofinal.CriarNotas
import com.example.trabalhofinal.R
import com.example.trabalhofinal.databinding.FragmentBloconotasBinding
import com.example.trabalhofinal.model.Note
import com.example.trabalhofinal.model.NoteResponse
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [BlocoNotas.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlocoNotas : Fragment() {
    private lateinit var binding: FragmentBloconotasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Use o layout inflater apropriado para o fragmento BlocoNotas
        binding = FragmentBloconotasBinding.inflate(inflater, container, false)
        val view = binding.root


        setupButtonListeners()
        val userId = getSavedUserId()

       // val btnCreateNote: Button = binding.btnCreateNoteFromView
        //btnCreateNote.setOnClickListener {
          //  getNotes()
            // startActivity(Intent(this@Bnotas, CriarNotas::class.java))
        //}

        return view
    }


    private fun getNotes() {
        val userId = getSavedUserId()

        // Verifica se o userId é válido
        if (userId.isNotEmpty()) {
            val call = RetrofitInitializer().service().getNotes("Bearer Tostas")

            call.enqueue(object : Callback<NoteResponse> {
                override fun onResponse(call: Call<NoteResponse>, response: Response<NoteResponse>) {
                    if (response.isSuccessful) {
                        val noteResponse = response.body()
                        if (noteResponse != null) {
                            val notes = noteResponse.notes
                            if (notes != null && notes.isNotEmpty()) {
                                // O usuário tem notas, faça algo com as notas obtidas da API
                                // Por exemplo, exibir ou processar as notas
                                Log.e("Notas", "O usuário tem notas")
                                Log.i("INFO", "Usuário encontrado: $notes")
                                // Converte a lista de notas em uma string
                                val notesString = notesToString(notes)

                                // Salva a string na SharedPreferences
                                saveNotesToSharedPreferences(notesString)

                                // Inicie a atividade CriarNotas após obter as notas
                                // Substitua o nome do fragmento e a transação de fragmento conforme necessário
                                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.fragment_container, BlocoNotas.newInstance())
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
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
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("notes", notesString)
        editor.apply()
    }

    private fun getSavedUserId(): String {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }

    private fun handleMenuItemClick(itemId: Int): Boolean {
        // Implemente a lógica para lidar com os itens do menu de navegação, se necessário
        return true
    }
    private fun abrirCriarNotas() {
        val balanceViewFragment = CriarNotas()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }

    private fun setupButtonListeners() {
        val btnIrCriarNotas = binding.btnCreateNoteFromView
        btnIrCriarNotas.setOnClickListener {
            abrirCriarNotas()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BlocoNotas()
    }
}
