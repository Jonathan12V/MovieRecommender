package com.example.movierecommender

import UserInfo
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.movierecommender.views.PeliculasRecomendadasActivity
import com.example.movierecommender.views.PeliculasRecomendadasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

abstract class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private var manualClose = false
    private var modoOscuroModalAbierto = false
    private var selectedItem: MenuItem? = null
    private var selectedBottomNavItem: Int = 0
    private lateinit var fab: FloatingActionButton

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        drawerLayout = findViewById(R.id.drawer_layout)

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            showModoOscuroBottomSheet()
        }

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0)
        val navHeaderTextView : TextView = headerView.findViewById(R.id.nav_header_textView)
        navHeaderTextView.text = UserInfo.username

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->

            selectedBottomNavItem = menuItem.itemId
            when (menuItem.itemId) {
                R.id.bottom_accion_regreso_inicio -> {
                    val fragment = PeliculasRecomendadasFragment()
                    replaceFragment(fragment)
                    true
                }
                R.id.bottom_accion_perfil -> {

                    val fragment = VisualizacionPerfilFragment()
                    replaceFragment(fragment)
                    true
                }
                R.id.bottom_accion_favoritos -> {
                    val fragment = PeliculasFavoritasFragment()
                    replaceFragment(fragment)
                    true
                }
                else -> false
            }
        }

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
            // Reemplazar el contenido del contenedor principal con LoginFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PeliculasRecomendadasFragment())
                .commit()
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

    // Agrega esta función en tu clase MenuActivity para reemplazar el contenido del contenedor principal con un fragmento
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Reemplaza "R.id.container" con el ID del contenedor donde deseas mostrar el fragmento
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.accion_perfil -> {
                val fragment = VisualizacionPerfilFragment()
                replaceFragment(fragment)
                Toast.makeText(this, "Visualización perfil", Toast.LENGTH_SHORT).show();
                true
            }
            R.id.accion_favoritos -> {
                val fragment = PeliculasFavoritasFragment()
                replaceFragment(fragment)
                Toast.makeText(this, "Tus peliculas favoritas", Toast.LENGTH_SHORT).show();
                true
            }
            R.id.accion_cerrar_sesion -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
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
        modoOscuroModalAbierto = true
        val dialogView = layoutInflater.inflate(R.layout.tu_layout_bottom_sheet_modo_oscuro, null)
        val bottomSheetDialog = BottomSheetDialog(this)

        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()

        val rgModoOscuro = dialogView.findViewById<RadioGroup>(R.id.rgModoOscuro)
        val rgTemaOscuro = dialogView.findViewById<RadioGroup>(R.id.rgTemaOscuro)

        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val selectedTheme = sharedPreferences.getInt("selectedTheme", R.style.Tema_claro)
        val isDarkMode = selectedTheme == R.style.Tema_azul_oscuro

        rgModoOscuro.check(if (isDarkMode) R.id.rbActivado else R.id.rbDesactivado)
        rgTemaOscuro.check(if (isDarkMode) R.id.rbNocheOscura else R.id.rbNocheClara)

        rgModoOscuro.setOnCheckedChangeListener { _, checkedId ->
            rgTemaOscuro.isEnabled = checkedId == R.id.rbActivado
        }

        rgTemaOscuro.setOnCheckedChangeListener { _, checkedId ->
            if (rgModoOscuro.checkedRadioButtonId == R.id.rbActivado) {
                val newTheme = if (checkedId == R.id.rbNocheOscura) {
                    R.style.Tema_oscuro
                } else {
                    R.style.Tema_claro
                }

                sharedPreferences.edit().putInt("selectedTheme", newTheme).apply()

                val logoImageView: ImageView? = findViewById(R.id.logo)
                val menuImageView: ImageView? = findViewById(R.id.menu)

                if (newTheme == R.style.Tema_oscuro) {
                    logoImageView?.setImageResource(R.mipmap.icono_white)
                    menuImageView?.setImageResource(R.drawable.menu_blank)
                } else {
                    logoImageView?.setImageResource(R.mipmap.icono)
                    menuImageView?.setImageResource(R.drawable.menu)
                }

                setTheme(newTheme)
                recreate()
            }
        }

        bottomSheetDialog.setOnDismissListener {
            modoOscuroModalAbierto = false
        }
    }

}