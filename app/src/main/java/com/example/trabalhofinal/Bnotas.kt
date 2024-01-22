package com.example.trabalhofinal

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.trabalhofinal.databinding.ActivityBnotasBinding
import com.example.trabalhofinal.model.Note
import com.example.trabalhofinal.model.NoteRequest
import com.example.trabalhofinal.model.NoteResponse
import com.example.trabalhofinal.model.UserRequest
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Bnotas : TesteMenu() {

    private lateinit var binding: ActivityBnotasBinding
    private lateinit var dbHelper: BDNotas
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use o layout inflater apropriado para a atividade Bnotas
        binding = ActivityBnotasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem.itemId)
        }

        dbHelper = BDNotas(this)
        linearLayout = findViewById(R.id.layout)

        // Exibindo notas existentes
      //  exibirNotas()
        val userId = getSavedUserId()

        val btnCreateNote: Button = findViewById(R.id.btnCreateNoteFromView)
        btnCreateNote.setOnClickListener {
            getNotes()
       //     startActivity(Intent(this@Bnotas, CriarNotas::class.java))
        }
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
                                startActivity(Intent(this@Bnotas, CriarNotas::class.java))


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




    private fun getSavedUserId(): String {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }


  //  override fun onBackPressed() {
  //      val intent = Intent(this, MainActivity::class.java)
  //      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  //      startActivity(intent)
  //      finish()
  //  }

  //  private fun exibirNotas() {
  //      val db: SQLiteDatabase = dbHelper.readableDatabase
  //      val projection = arrayOf(BDNotas.COLUMN_ID, BDNotas.COLUMN_NOTE)
  //      val cursor: Cursor = db.query(
  //          BDNotas.TABLE_NOTES,
  //          projection,
  //          null,
  //          null,
  //          null,
  //          null,
  //          null
  //      )
//
  //      while (cursor.moveToNext()) {
  //          val noteId: Int = cursor.getInt(cursor.getColumnIndexOrThrow(BDNotas.COLUMN_ID))
  //          val note: String = cursor.getString(cursor.getColumnIndexOrThrow(BDNotas.COLUMN_NOTE))
//
  //          val textViewNote = TextView(this)
  //          textViewNote.text = note
  //          textViewNote.tag = noteId.toString()
//
  //          val btnDeleteNote = Button(this)
  //          btnDeleteNote.text = "Delete"
  //          btnDeleteNote.tag = noteId.toString()
  //          btnDeleteNote.setOnClickListener {
  //              deletarNota(noteId)
  //          }
//
  //          linearLayout.addView(textViewNote)
  //          linearLayout.addView(btnDeleteNote)
  //      }
//
  //      cursor.close()
//
  //      Log.d("Bnotas", "Exibição de notas concluída")
  //  }
//
  //  private fun deletarNota(noteId: Int) {
  //      val db: SQLiteDatabase = dbHelper.writableDatabase
  //      val selection = "${BDNotas.COLUMN_ID} = ?"
  //      val selectionArgs = arrayOf(noteId.toString())
  //      db.delete(BDNotas.TABLE_NOTES, selection, selectionArgs)
//
  //      // Encontre a View correspondente e remova-a do LinearLayout
  //      val viewToRemove = linearLayout.findViewWithTag<View>(noteId.toString())
  //      linearLayout.removeView(viewToRemove)
  //  }
}
