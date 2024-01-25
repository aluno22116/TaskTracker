package com.example.trabalhofinal

import BlocoNotas
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.trabalhofinal.model.Note
import com.example.trabalhofinal.model.NoteRequest
import com.example.trabalhofinal.model.NoteResponse
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CriarNotas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_criar_notas, container, false)
        setupButtonListeners(view)
        return view
    }

    private fun getSavedUserId(): String {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }

    private fun getSavedNotes(): String {
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("notes", "") ?: ""
    }

    private fun setupButtonListeners(view: View) {
        val userId = getSavedUserId()
        val savedNotes = getSavedNotes()

        val btnSalvarNota = view.findViewById<Button>(R.id.btnSalvarNota)
        val btnApagarNota = view.findViewById<Button>(R.id.btnApagarNota)

        btnSalvarNota.setOnClickListener {
            if (userId.isNotEmpty()) {
                Log.i("ALERTA!!!!!!!!", "$savedNotes")
                updateNotes(userId)
            }
        }

        btnApagarNota.setOnClickListener {

        }
    }

    private fun updateNotes(userId: String) {
        val editarTexto = view?.findViewById<EditText>(R.id.novaNota)
        val textoNota = editarTexto?.text.toString()
        val nota = Note(null, null, textoNota, null)
        val notes = NoteRequest(nota)
        val putCall = RetrofitInitializer().service().updateNote("Bearer Tostas", userId, notes)
        putCall.enqueue(object : Callback<NoteRequest> {
            override fun onResponse(call: Call<NoteRequest>, response: Response<NoteRequest>) {
                if (response.isSuccessful) {
                    Log.i("ALERTA!!!!!!!!", "Nota colocada na API")


                    getNotes(userId)

                } else {
                    Log.e("Erro", "Erro na chamada à API de atualização de nota: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NoteRequest>, t: Throwable) {
                t.printStackTrace()
                Log.e("Erro", "Erro na chamada à API de atualização de nota: ${t.message}")
            }
        })
    }

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

                                // Substituir ou adicionar o fragmento BlocoNotas
                                val blocoNotasFragment = BlocoNotas()
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.fragment_container, blocoNotasFragment)
                                transaction.addToBackStack(null)
                                transaction.commit()
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

    // Função para salvar a string na SharedPreferences
    private fun saveNotesToSharedPreferences(notesString: String) {
        val sharedPreferences =  requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("notes", notesString)
        editor.apply()
    }


    private fun notesToString(notes: List<Note>): String {
        return notes.joinToString { it.toString() }
    }
}
