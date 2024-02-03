// PeliculasRecomendadasActivity.kt
package com.example.movierecommender.views

import android.os.Bundle
import com.example.movierecommender.MenuActivity
import com.example.movierecommender.R

class PeliculasRecomendadasActivity : MenuActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Reemplazar el contenido del contenedor principal con LoginFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PeliculasRecomendadasFragment())
            .commit()
    }
}