package com.example.movierecommender

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class VisualizacionPerfilActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsContent = layoutInflater.inflate(R.layout.activity_visualizar_perfil, null)
        findViewById<ConstraintLayout>(R.id.fragment_container).addView(settingsContent)

        val btnEditarPerfil = findViewById<Button>(R.id.btnEditarPerfil)

        btnEditarPerfil.setOnClickListener {
            val intent = Intent(this, EditPerfilActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Editar Perfil", Toast.LENGTH_SHORT).show()
        }

    }
}