package com.example.movierecommender

import AESEncryption
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movierecommender.repository.UserRepository

class Register1Activity : AppCompatActivity() {


    lateinit var etUser: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var buttonGuardar: Button

    private lateinit var userRepository: UserRepository

    private val AES: AESEncryption = AESEncryption()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro1)

        etUser = findViewById(R.id.etUser)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        buttonGuardar = findViewById(R.id.buttonGuardar)

        userRepository = UserRepository(this)


        buttonGuardar.setOnClickListener() {

            if (etUser.text.isEmpty() || etEmail.text.isEmpty() || etPassword.text.isEmpty()) {
                validacion();
            } else {
                val check = userRepository.checkUserAndEmailUsado(
                    etUser.text.toString(),
                    etEmail.text.toString()
                )
                val checkPassword = userRepository.checkPasswordSecurity(etPassword.text.toString())
                val checkEmailEstructura =
                    userRepository.checkEmailEstructura(etEmail.text.toString())

                when (check) {
                    1 -> {
                        etUser.setError("Ese nombre de usuario ya está en uso.")
                    }

                    2 -> etEmail.setError("Ese e-mail ya está en uso")
                    3 -> {
                        etUser.setError("Ese nombre de usuario ya esta en uso")
                        etPassword.setError("El e-mail ya esta en uso")
                    }
                    -1 -> {
                        if (!checkPassword) {
                            etPassword.setError("La contraseña no cumple los requisitos.")
                        }

                        if (!checkEmailEstructura) {
                            etEmail.setError("El email no tiene una estructura correcta")
                        }
                        if (checkPassword && checkEmailEstructura) {
                            val encryptedPassword = AES.encrypt(etPassword.text.toString());
                            userRepository.addUser(etUser.text.toString(), etEmail.text.toString(), encryptedPassword)

                            Toast.makeText(this, "Te has registrado correctamente", Toast.LENGTH_LONG).show();

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@Register1Activity, message, Toast.LENGTH_LONG).show()
    }

    private fun validacion() {
        val nombre: String = etUser.getText().toString()
        val apellido: String = etEmail.getText().toString()
        val password: String = etPassword.getText().toString()
        if (nombre == "") {
            etUser.setError("Campo requerido")
        }
        if (apellido == "") {
            etEmail.setError("Campo requerido")
        }
        if (password == "") {
            etPassword.setError("Campo requerido")
        }
    }

}

