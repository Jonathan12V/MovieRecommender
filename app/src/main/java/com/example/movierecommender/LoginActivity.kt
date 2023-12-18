package com.example.movierecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.movierecommender.views.PeliculasRecomendadasActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var tvRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvRegistro=findViewById(R.id.tvRegistre)

        val btnIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)

        btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, PeliculasRecomendadasActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Mostrando peliculas recomendadas", Toast.LENGTH_SHORT).show()
        }
    }

    fun irRegistro(view: View) {
        val intent = Intent(this, Register1Activity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Registrar usuario", Toast.LENGTH_SHORT).show()
    }
}