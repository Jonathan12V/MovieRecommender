package com.example.movierecommender

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout

class EditPerfilActivity : AppCompatActivity() {

    private lateinit var autoCompleteTextView : AutoCompleteTextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil1)

        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        autoCompleteTextView = textInputLayout.findViewById(R.id.autoCompleteTextView)

        autoCompleteTextView.setOnClickListener {
            showPeliculasBottomSheet()
        }
    }

    fun showPeliculasBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_peliculas, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }
}
