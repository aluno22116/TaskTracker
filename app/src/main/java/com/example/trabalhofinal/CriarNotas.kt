package com.example.trabalhofinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class CriarNotas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criar_notas, container, false)

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        val btnSalvarNota = view?.findViewById<Button>(R.id.btnSalvarNota)
        val btnApagarNota = view?.findViewById<Button>(R.id.btnApagarNota)


        btnSalvarNota?.setOnClickListener{

        }

        btnApagarNota?.setOnClickListener{

        }
    }

}