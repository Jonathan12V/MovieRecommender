package com.example.movierecommender

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class PeliculasFavoritasActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsContent = layoutInflater.inflate(R.layout.activity_peliculas_favoritas, null)
        findViewById<ConstraintLayout>(R.id.fragment_container).addView(settingsContent)
    }
}