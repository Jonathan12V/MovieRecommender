package com.example.movierecommender

import AESEncryption
import UserInfo
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.movierecommender.repository.UserRepository
import com.example.movierecommender.views.PeliculasRecomendadasActivity
import com.example.movierecommender.views.PeliculasRecomendadasFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var tvRegistro: TextView
    lateinit var buttonIniciarSesion: Button

    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText

    private lateinit var userRepository: UserRepository
    private lateinit var AES: AESEncryption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvRegistro = findViewById(R.id.tvRegistre)
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion)
        etUser = findViewById(R.id.etUser)
        etPassword = findViewById(R.id.etPassword)

        userRepository = UserRepository(this)
        AES = AESEncryption()

        buttonIniciarSesion.setOnClickListener {

            val passwordEncrypted = AES.encrypt(etPassword.text.toString())

            val check = userRepository.checkUser(etUser.text.toString(), passwordEncrypted)

            if (check == 1) {

                UserInfo.username = etUser.text.toString()
                UserInfo.id = userRepository.getIdUser(etUser.text.toString())

                val intent = Intent(this, PeliculasRecomendadasActivity::class.java)
                startActivity(intent)
            } else if (check == 2) {
                Toast.makeText(
                    this@LoginActivity,
                    "No existe ese usuario",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Contraseña incorrecta",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun irRegistro(view: View) {

        val intent = Intent(this, Register1Activity::class.java)
        startActivity(intent)
    }
}