package com.example.movierecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.movierecommender.views.PeliculasRecomendadasActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView

abstract class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private var manualClose = false

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
            Toast.makeText(this, "Inicio - peliculas recomendadas", Toast.LENGTH_SHORT).show()
        }

        navigationView.setNavigationItemSelectedListener(this)

        // Agregar listener para detectar la apertura y cierre del DrawerLayout
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: android.view.View) {
                super.onDrawerOpened(drawerView)
                // El menú se ha abierto manualmente
                manualClose = false
                // El menú se ha abierto manualmente
                Toast.makeText(this@MenuActivity, "Menú abierto", Toast.LENGTH_SHORT).show()
            }

            override fun onDrawerClosed(drawerView: android.view.View) {
                super.onDrawerClosed(drawerView)
                // El menú se ha cerrado manualmente
                if (!manualClose) {
                    Toast.makeText(this@MenuActivity, "Menú cerrado", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accion_perfil -> {
                openActivity(VisualizacionPerfilActivity::class.java)
                Toast.makeText(this, "Visualización perfil", Toast.LENGTH_SHORT).show();
            }
            R.id.accion_favoritos -> {
                openActivity(PeliculasFavoritasActivity::class.java)
                Toast.makeText(this, "Tus peliculas favoritas", Toast.LENGTH_SHORT).show();
            }
            R.id.accion_cerrar_sesion -> {
                openActivity(LoginActivity::class.java)
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            }
            R.id.accion_cambiar_tema -> {
                Toast.makeText(this, "Configuración de Modo Oscuro", Toast.LENGTH_SHORT).show();
                showModoOscuroBottomSheet()
            }
        }

        manualClose = true
        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    private fun openActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    fun showModoOscuroBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.tu_layout_bottom_sheet_modo_oscuro, null)
        val bottomSheetDialog = BottomSheetDialog(this)

        // Verificar si la actividad está en un estado válido
        if (!isFinishing && !isDestroyed) {
            bottomSheetDialog.setContentView(dialogView)

            val rgModoOscuro = dialogView.findViewById<RadioGroup>(R.id.rgModoOscuro)
            rgModoOscuro.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbDesactivado -> {
                        // Lógica para Modo Oscuro desactivado (si es necesario)
                        setTheme(R.style.Tema_claro)
                        recreate()
                    }
                    R.id.rbActivado -> {
                        // Cambiar el tema a oscuro
                        setTheme(R.style.Tema_oscuro)
                        recreate()
                        // Otras acciones que desees realizar cuando se activa el modo oscuro
                        Toast.makeText(this, "Modo Oscuro Activado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            bottomSheetDialog.show()
        }
    }


}
