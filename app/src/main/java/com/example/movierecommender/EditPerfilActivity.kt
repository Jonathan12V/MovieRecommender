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
    private lateinit var autoCompleteTextView2 : AutoCompleteTextView
    private lateinit var autoCompleteTextView3 : AutoCompleteTextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil1)

        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        autoCompleteTextView = textInputLayout.findViewById(R.id.autoCompleteTextView)

        val textInputLayout2 = findViewById<TextInputLayout>(R.id.textInputLayout2)
        autoCompleteTextView2 = textInputLayout2.findViewById(R.id.autoCompleteTextView2)

        val textInputLayout3 = findViewById<TextInputLayout>(R.id.textInputLayout3)
        autoCompleteTextView3 = textInputLayout3.findViewById(R.id.autoCompleteTextView3)

        autoCompleteTextView.setOnClickListener {
            showPeliculasBottomSheet()
        }

        autoCompleteTextView2.setOnClickListener {
            showActoresBottomSheet()
        }

        autoCompleteTextView3.setOnClickListener {
            showGenerosBottomSheet()
        }
    }

    fun showPeliculasBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_peliculas, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }

    fun showActoresBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_actores, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }

    fun showGenerosBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_generos, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }
}
