package com.example.trabalhofinal

import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.navigation.NavigationView

class Calendario : TesteMenu() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        val calendar = findViewById<DatePicker>(R.id.calendar)
        val caixaTextoCalendar = findViewById<EditText>(R.id.caixaTextoCalendar)

        // Configurar um Listener para o DatePicker
        calendar.init(calendar.year, calendar.month, calendar.dayOfMonth,
            DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                // Aqui você pode lidar com a data selecionada
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                Toast.makeText(this, "Data selecionada: $selectedDate", Toast.LENGTH_SHORT).show()

                // Habilitar a caixa de texto para anotações
                caixaTextoCalendar.isEnabled = true
            })

        // Configurar o NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem.itemId)
        }
    }
}
