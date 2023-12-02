package com.example.movierecommender

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class VisualizacionPerfilActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsContent = layoutInflater.inflate(R.layout.activity_visualizar_perfil, null)
        findViewById<ConstraintLayout>(R.id.fragment_container).addView(settingsContent)
    }
}