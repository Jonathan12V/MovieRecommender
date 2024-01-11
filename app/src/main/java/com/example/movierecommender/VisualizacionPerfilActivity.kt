package com.example.movierecommender

import UserInfo
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.movierecommender.repository.UserRepository

class VisualizacionPerfilActivity : MenuActivity() {

    private lateinit var tvUser: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUser2: TextView

    private lateinit var userRepository: UserRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsContent = layoutInflater.inflate(R.layout.activity_visualizar_perfil, null)
        findViewById<ConstraintLayout>(R.id.fragment_container).addView(settingsContent)

        tvUser = findViewById(R.id.tvNombrePerfil)
        tvEmail = findViewById(R.id.tvEmail)
        tvUser2 = findViewById(R.id.tvUser2)

        userRepository = UserRepository(this)


        val btnEditarPerfil = findViewById<Button>(R.id.btnEditarPerfil)

        tvUser.setText(UserInfo.username)
        tvUser2.setText(UserInfo.username)
        tvEmail.setText(userRepository.verEmail())

        btnEditarPerfil.setOnClickListener {
            val intent = Intent(this, EditPerfilActivity::class.java)
            startActivity(intent)
        }

    }
}