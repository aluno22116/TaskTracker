package com.example.trabalhofinal;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CriarNotas extends AppCompatActivity {

    private BDNotas dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criarnotas);

        dbHelper = new BDNotas(this);

        EditText editTextNote = findViewById(R.id.editarTexto);
        Button btnSave = findViewById(R.id.btnSalvar);

        btnSave.setOnClickListener(view -> salvarNotaNoBancoDeDados(editTextNote.getText().toString()));
    }

    private void salvarNotaNoBancoDeDados(String nota) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BDNotas.COLUMN_NOTE, nota);

        long newRowId = db.insert(BDNotas.TABLE_NOTES, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Nota salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar nota.", Toast.LENGTH_SHORT).show();
        }
    }
}
