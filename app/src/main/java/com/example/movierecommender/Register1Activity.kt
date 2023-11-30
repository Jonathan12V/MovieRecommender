package com.example.movierecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Register1Activity : AppCompatActivity() {

    private lateinit var tvRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvRegistro=findViewById(R.id.tvRegistre)

        }

    private fun irRegistro(){
        val intent = Intent(this, Register1Activity)
        startActivity(intent)
    }
}