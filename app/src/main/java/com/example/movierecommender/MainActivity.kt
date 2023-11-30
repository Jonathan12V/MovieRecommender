package com.example.movierecommender

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peliculas_todas)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.accion_favoritos -> {
                Toast.makeText(this, "Hola", Toast.LENGTH_LONG).show();
                //setContentView(R.layout.activity_peliculas_favoritas)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
