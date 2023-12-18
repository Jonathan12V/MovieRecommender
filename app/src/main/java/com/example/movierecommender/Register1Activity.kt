package com.example.movierecommender

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register1Activity : AppCompatActivity() {


    lateinit var etUser: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var buttonSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro1)

        etUser = findViewById(R.id.etUser)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        buttonSiguiente = findViewById(R.id.buttonSiguiente)

        buttonSiguiente.setOnClickListener() {

            if (etUser.text.toString().isEmpty() ||
                etEmail.text.toString().isEmpty() ||
                etPassword.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this@Register1Activity,
                    "Hay que rellenar todos los apartados",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                val intent = Intent(this, Register2Activity::class.java)
                intent.putExtra("user", etUser.text.toString())
                intent.putExtra("email", etEmail.text.toString())
                intent.putExtra("password", etPassword.text.toString())

                startActivity(intent)

            }
        }
    }
}
