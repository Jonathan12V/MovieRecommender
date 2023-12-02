package com.example.movierecommender

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class CerrarSesionActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsContent = layoutInflater.inflate(R.layout.activity_cerrar_sesion, null)
        findViewById<ConstraintLayout>(R.id.fragment_container).addView(settingsContent)
    }
}