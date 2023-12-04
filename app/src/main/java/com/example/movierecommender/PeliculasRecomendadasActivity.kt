package com.example.movierecommender

import android.os.Bundle
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout

class PeliculasRecomendadasActivity : MenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsContent = layoutInflater.inflate(R.layout.activity_peliculas_recomendadas, null)
        findViewById<ConstraintLayout>(R.id.fragment_container).addView(settingsContent)

        val buttonTodo: RadioButton = findViewById(R.id.Todo);
        val buttonRecomendadas: RadioButton = findViewById(R.id.recomendados);
        buttonTodo.setOnClickListener {
            buttonTodo.isChecked = true;
            buttonRecomendadas.isChecked = false;
        }
        buttonRecomendadas.setOnClickListener {
            buttonRecomendadas.isChecked = true;
            buttonTodo.isChecked = false;
        }
    }
}