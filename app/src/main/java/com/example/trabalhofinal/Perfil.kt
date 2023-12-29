package com.example.trabalhofinal

import android.os.Bundle
import com.example.trabalhofinal.databinding.ActivityPerfilBinding

class Perfil : TesteMenu() {
    private lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
