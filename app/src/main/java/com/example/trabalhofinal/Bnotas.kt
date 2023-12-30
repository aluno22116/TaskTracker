package com.example.trabalhofinal

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
import com.google.android.material.navigation.NavigationView

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
        exibirNotas()

        val btnCreateNote: Button = findViewById(R.id.btnCreateNoteFromView)
        btnCreateNote.setOnClickListener {
            startActivity(Intent(this@Bnotas, CriarNotas::class.java))
        }
    }

    private fun exibirNotas() {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val projection = arrayOf(BDNotas.COLUMN_ID, BDNotas.COLUMN_NOTE)
        val cursor: Cursor = db.query(
            BDNotas.TABLE_NOTES,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val noteId: Int = cursor.getInt(cursor.getColumnIndexOrThrow(BDNotas.COLUMN_ID))
            val note: String = cursor.getString(cursor.getColumnIndexOrThrow(BDNotas.COLUMN_NOTE))

            val textViewNote = TextView(this)
            textViewNote.text = note
            textViewNote.tag = noteId.toString()

            val btnDeleteNote = Button(this)
            btnDeleteNote.text = "Delete"
            btnDeleteNote.tag = noteId.toString()
            btnDeleteNote.setOnClickListener {
                deletarNota(noteId)
            }

            linearLayout.addView(textViewNote)
            linearLayout.addView(btnDeleteNote)
        }

        cursor.close()

        Log.d("Bnotas", "Exibição de notas concluída")
    }

    private fun deletarNota(noteId: Int) {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val selection = "${BDNotas.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(noteId.toString())
        db.delete(BDNotas.TABLE_NOTES, selection, selectionArgs)

        // Encontre a View correspondente e remova-a do LinearLayout
        val viewToRemove = linearLayout.findViewWithTag<View>(noteId.toString())
        linearLayout.removeView(viewToRemove)
    }
}
