package com.example.trabalhofinal

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trabalhofinal.model.Note
import com.example.trabalhofinal.model.NoteRequest
import com.example.trabalhofinal.model.User
import com.example.trabalhofinal.model.UserRequest
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CriarNotas : AppCompatActivity() {
    private var dbHelper: BDNotas? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criarnotas)

        // Recuperar userId do SharedPreferences
        val userId = getSavedUserId()
        val btnSave = findViewById<Button>(R.id.btnSalvar)

        val savedNotes = getSavedNotes()


        btnSave.setOnClickListener{
            if (userId != null) {

                Log.i("ALERTA!!!!!!!!","$savedNotes")
                updateNotes(userId)
            }
        }

    }
    private fun getSavedUserId(): String {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }
    private fun getSavedNotes(): String {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        // Recupera a string de notas da SharedPreferences, use um valor padrão vazio se não houver notas
        return sharedPreferences.getString("notes", "") ?: ""
    }

    


    // Exemplo de chamada para atualizar uma nota
    private fun updateNotes(userId: String) {
        val editarTexto = findViewById<EditText>(R.id.editarTexto)
        val textoNota = editarTexto.text.toString()
        val nota = Note(null,null, textoNota)
        val notes = NoteRequest(nota)
        val putCall = RetrofitInitializer().service().updateNote("Bearer Tostas", userId, notes)
        putCall.enqueue(object : Callback<NoteRequest> {
            override fun onResponse(call: Call<NoteRequest>, response: Response<NoteRequest>) {
                if (response.isSuccessful) {
                    // Trate a resposta bem-sucedida conforme necessário
                    Log.i("ALERTA!!!!!!!!","Nota colocada na API")
                    abrirBNotas()
                } else {
                    Log.e("Erro", "Erro na chamada à API de atualização de nota: ${response.message()}")
                    // Lide com o erro conforme necessário
                }
            }

            override fun onFailure(call: Call<NoteRequest>, t: Throwable) {
                t.printStackTrace()
                Log.e("Erro", "Erro na chamada à API de atualização de nota: ${t.message}")
                // Lide com o erro conforme necessário
            }
        })
    }


    private fun abrirBNotas() {
        val intent = Intent(this, Bnotas::class.java)
        startActivity(intent)

        // Remove a atividade CriarNotas da pilha
        finish()
    }
}
