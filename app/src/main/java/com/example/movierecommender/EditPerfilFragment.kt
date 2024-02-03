package com.example.movierecommender

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.movierecommender.repository.UserRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout

class EditPerfilFragment : Fragment() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var autoCompleteTextView2: AutoCompleteTextView
    private lateinit var autoCompleteTextView3: AutoCompleteTextView

    lateinit var buttonGuardar: Button

    private lateinit var userRepository: UserRepository

    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_editar_perfil1, container, false)

        val textInputLayout = view.findViewById<TextInputLayout>(R.id.textInputLayout)
        autoCompleteTextView = textInputLayout.findViewById(R.id.autoCompleteTextView)

        val textInputLayout2 = view.findViewById<TextInputLayout>(R.id.textInputLayout2)
        autoCompleteTextView2 = textInputLayout2.findViewById(R.id.autoCompleteTextView2)

        val textInputLayout3 = view.findViewById<TextInputLayout>(R.id.textInputLayout3)
        autoCompleteTextView3 = textInputLayout3.findViewById(R.id.autoCompleteTextView3)

        etUser = view.findViewById(R.id.etUsuario)
        etPassword = view.findViewById(R.id.etContraseña)
        etEmail = view.findViewById(R.id.etEmail)

        buttonGuardar = view.findViewById(R.id.buttonSave)

        userRepository = UserRepository(requireContext())

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
                requireContext(),
                "Datos actualizados",
                Toast.LENGTH_LONG
            ).show()
        }

        return view
    }

    private fun showPeliculasBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_peliculas, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }

    private fun showActoresBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_actores, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }

    private fun showGenerosBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_generos, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }
}
