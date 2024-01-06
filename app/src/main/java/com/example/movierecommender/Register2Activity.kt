package com.example.movierecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.movierecommender.repository.UserRepository

class Register2Activity : AppCompatActivity() {


    lateinit var buttonRegistro: Button
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro2)

        userRepository = UserRepository(this)

        val user = intent.getStringExtra("user")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        buttonRegistro = findViewById(R.id.buttonRegistro)

        buttonRegistro.setOnClickListener() {

            if (user != null && email != null && password != null) {
                userRepository.addUser(user, email, password)

                Toast.makeText(
                    this@Register2Activity,
                    "Se ha añadido a la DB el usuario $user - $email - $password",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }
}