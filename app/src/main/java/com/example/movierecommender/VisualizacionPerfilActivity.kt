package com.example.movierecommender

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.movierecommender.repository.UserRepository
import com.example.movierecommender.views.PeliculasRecomendadasFragment

class VisualizacionPerfilFragment : Fragment() {

    private lateinit var tvUser: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUser2: TextView
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_visualizar_perfil, container, false)

        tvUser = rootView.findViewById(R.id.tvNombrePerfil)
        tvEmail = rootView.findViewById(R.id.tvEmail)
        tvUser2 = rootView.findViewById(R.id.tvUser2)

        userRepository = UserRepository(requireContext())

        val btnEditarPerfil = rootView.findViewById<Button>(R.id.btnEditarPerfil)

        tvUser.text = UserInfo.username
        tvUser2.text = UserInfo.username
        tvEmail.text = userRepository.verEmail()

        btnEditarPerfil.setOnClickListener {
            // Reemplazar el contenido del contenedor principal con LoginFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditPerfilFragment())
                .commit()
        }

        return rootView
    }
}
