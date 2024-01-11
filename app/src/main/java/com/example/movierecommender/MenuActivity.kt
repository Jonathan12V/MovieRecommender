package com.example.movierecommender

import UserInfo
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

abstract class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)


        val menuButton: ImageView = findViewById(R.id.menu)
        menuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        val homePeliculas: ImageView = findViewById(R.id.logo)
        homePeliculas.setOnClickListener {
            val intent = Intent(this, PeliculasRecomendadasActivity::class.java)
            startActivity(intent)
        }

        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accion_perfil -> openActivity(VisualizacionPerfilActivity::class.java)
            R.id.accion_favoritos -> openActivity(PeliculasFavoritasActivity::class.java)
            R.id.accion_cerrar_sesion -> openActivity(LoginActivity::class.java)
            // Agregar más casos según sea necesario
        }

        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    private fun openActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
