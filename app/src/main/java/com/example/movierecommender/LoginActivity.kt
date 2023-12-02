package com.example.movierecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    private lateinit var tvRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvRegistro=findViewById(R.id.tvRegistre)

        val btnIniciarSesion = findViewById<Button>(R.id.buttonIniciarSesion)

        btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, PeliculasTodasActivity::class.java)
            startActivity(intent)
        }
    }

    fun irRegistro(view: View) {
        val intent = Intent(this, Register1Activity::class.java)
        startActivity(intent)
    }
}