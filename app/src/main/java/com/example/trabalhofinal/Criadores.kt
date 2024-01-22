package com.example.trabalhofinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Criadores : Fragment() {

    // Este método é chamado quando o fragmento deve criar sua view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criadores, container, false)
    }

    // O método onViewCreated é chamado após a criação da view do fragmento
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aqui você pode configurar as visualizações dentro do fragmento, se necessário
    }
}
