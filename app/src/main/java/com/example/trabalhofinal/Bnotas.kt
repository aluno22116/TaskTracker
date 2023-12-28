package com.example.trabalhofinal
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Bnotas : AppCompatActivity() {

    private lateinit var dbHelper: BDNotas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bnotas)

        dbHelper = BDNotas(this)

        val textViewNotes: TextView = findViewById(R.id.textViewNotes)
        val btnCreateNote: Button = findViewById(R.id.btnCreateNoteFromView)

        // Exibindo notas existentes
        exibirNotas()

        btnCreateNote.setOnClickListener { startActivity(Intent(this@Bnotas, CriarNotas::class.java)) }
    }

    private fun exibirNotas() {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val projection = arrayOf(BDNotas.COLUMN_NOTE)
        val cursor: Cursor = db.query(
            BDNotas.TABLE_NOTES,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val notesText = StringBuilder()

        while (cursor.moveToNext()) {
            val note: String = cursor.getString(cursor.getColumnIndexOrThrow(BDNotas.COLUMN_NOTE))
            notesText.append(note).append("\n")
        }

        cursor.close()

        val textViewNotes: TextView = findViewById(R.id.textViewNotes)
        textViewNotes.text = notesText.toString()
    }
}
