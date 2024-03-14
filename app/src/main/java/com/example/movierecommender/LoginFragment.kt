package com.example.movierecommender

import AESEncryption
import UserInfo
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.movierecommender.repository.UserRepository
import com.example.movierecommender.views.PeliculasRecomendadasActivity

class LoginFragment : Fragment() {

    private lateinit var tvRegistro: TextView
    private lateinit var buttonIniciarSesion: Button
    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var userRepository: UserRepository

    private lateinit var AES: AESEncryption

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_login, container, false)

        tvRegistro = view.findViewById(R.id.tvRegistre)
        buttonIniciarSesion = view.findViewById(R.id.buttonIniciarSesion)
        etUser = view.findViewById(R.id.etUser)
        etPassword = view.findViewById(R.id.etPassword)

        userRepository = UserRepository(requireContext())
        AES = AESEncryption()


        buttonIniciarSesion.setOnClickListener {
            // Validación de campos
            if (etUser.text.isEmpty() || etPassword.text.isEmpty()) {
                validacion()
            } else {
                // Verificación de las credenciales del usuario
                val passwordEncrypted = AES.encrypt(etPassword.text.toString())

                val check = userRepository.checkUser(etUser.text.toString(), passwordEncrypted)


                when (check) {
                    1 -> {
                        // Usuario y contraseña correctos
                        UserInfo.username = etUser.text.toString()
                        UserInfo.id = userRepository.getIdUser(etUser.text.toString())

                        val intent =
                            Intent(requireContext(), PeliculasRecomendadasActivity::class.java)
                        startActivity(intent)
                    }

                    2 -> etUser.setError("No existe ese usuario")
                    else -> etPassword.setError("Contraseña incorrecta")
                }
            }
        }

        tvRegistro.setOnClickListener {
            val intent = Intent(requireContext(), Register1Activity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun validacion() {
        val nombre: String = etUser.getText().toString()
        val password: String = etPassword.getText().toString()
        if (nombre == "") {
            etUser.setError("Campo requerido")
        }
        if (password == "") {
            etPassword.setError("Campo requerido")
        }
    }
}