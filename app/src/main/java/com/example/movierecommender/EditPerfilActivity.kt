package com.example.movierecommender

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.movierecommender.repository.UserRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout

class EditPerfilActivity : MenuActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var autoCompleteTextView2: AutoCompleteTextView
    private lateinit var autoCompleteTextView3: AutoCompleteTextView

    lateinit var buttonGuardar: Button

    private lateinit var userRepository: UserRepository


    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsContent = layoutInflater.inflate(R.layout.activity_editar_perfil1, null)
        findViewById<ConstraintLayout>(R.id.fragment_container).addView(settingsContent)

        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        autoCompleteTextView = textInputLayout.findViewById(R.id.autoCompleteTextView)

        val textInputLayout2 = findViewById<TextInputLayout>(R.id.textInputLayout2)
        autoCompleteTextView2 = textInputLayout2.findViewById(R.id.autoCompleteTextView2)

        val textInputLayout3 = findViewById<TextInputLayout>(R.id.textInputLayout3)
        autoCompleteTextView3 = textInputLayout3.findViewById(R.id.autoCompleteTextView3)

        etUser = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etContraseña)
        etEmail = findViewById(R.id.etEmail)

        buttonGuardar = findViewById(R.id.buttonSave)

        userRepository = UserRepository(this)

        autoCompleteTextView.setOnClickListener {
            showPeliculasBottomSheet()
        }

        autoCompleteTextView2.setOnClickListener {
            showActoresBottomSheet()
        }

        autoCompleteTextView3.setOnClickListener {
            showGenerosBottomSheet()
        }

        buttonGuardar.setOnClickListener {
            val username = etUser.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            userRepository.cambiarCampos(username, email, password)

            Toast.makeText(
                this,
                "Datos actualizados",
                Toast.LENGTH_LONG
            ).show()
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
