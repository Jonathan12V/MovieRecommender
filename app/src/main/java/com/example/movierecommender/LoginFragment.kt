package com.example.movierecommender

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

        buttonIniciarSesion.setOnClickListener {
            val check = userRepository.checkUser(etUser.text.toString(), etPassword.text.toString())

            if (check == 1) {
                UserInfo.username = etUser.text.toString()
                UserInfo.id = userRepository.getIdUser(etUser.text.toString())

                val intent = Intent(requireContext(), PeliculasRecomendadasActivity::class.java)
                startActivity(intent)
            } else if (check == 2) {
                Toast.makeText(
                    requireContext(),
                    "No existe ese usuario",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Contraseña incorrecta",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        tvRegistro.setOnClickListener {
            val intent = Intent(requireContext(), Register1Activity::class.java)
            startActivity(intent)
        }

        return view
    }
}
