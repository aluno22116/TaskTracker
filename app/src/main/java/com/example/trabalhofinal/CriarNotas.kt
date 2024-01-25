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

                    // Substituir ou adicionar o fragmento BlocoNotas
                    val blocoNotasFragment = BlocoNotas()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, blocoNotasFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
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

    private fun notesToString(notes: List<Note>): String {
        return notes.joinToString { it.toString() }
    }
}
