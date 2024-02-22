package com.example.movierecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.databinding.MenuBottomPeliculasBinding
import com.example.movierecommender.models.PeliculaModel
import com.example.movierecommender.network.RetrofitClient
import com.example.movierecommender.repository.UserRepository
import com.example.movierecommender.viewmodels.PeliculasModalModel
import com.example.movierecommender.viewmodels.PeliculasViewModel
import com.example.movierecommender.views.AdapterNombrePeliculas
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout

class Register2Activity : AppCompatActivity() {


    lateinit var buttonRegistro: Button
    private lateinit var userRepository: UserRepository
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var autoCompleteTextView2: AutoCompleteTextView
    private lateinit var binding: MenuBottomPeliculasBinding
    private var listaPeliculas: List<PeliculaModel> = mutableListOf()// Declaración de variables
    private lateinit var adapter: AdapterNombrePeliculas
    private val viewModel: PeliculasModalModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro2)

        // Inflar el layout del menú bottom y asignarlo a la propiedad binding
        binding = MenuBottomPeliculasBinding.inflate(layoutInflater)

        // Observa la lista de películas en el ViewModel
        viewModel.peliculas.observe(this, { peliculas ->
            // Actualiza la lista de películas en el adaptador
            adapter.actualizarLista(peliculas)
        })

        userRepository = UserRepository(this)

        val user = intent.getStringExtra("user")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        buttonRegistro = findViewById(R.id.buttonRegistro)

        buttonRegistro.setOnClickListener() {

            if (user != null && email != null && password != null) {
                userRepository.addUser(user, email, password)

                Toast.makeText(
                    this@Register2Activity,
                    "Se ha añadido a la DB el usuario $user - $email - $password",
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }

        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayout)
        autoCompleteTextView = textInputLayout.findViewById(R.id.autoCompleteTextView)

        val textInputLayout2 = findViewById<TextInputLayout>(R.id.textInputLayout2)
        autoCompleteTextView2 = textInputLayout2.findViewById(R.id.autoCompleteTextView2)


        autoCompleteTextView.setOnClickListener {
            showPeliculasBottomSheet()
        }

        autoCompleteTextView2.setOnClickListener {
            showGenerosBottomSheet()
        }

    }

    private fun showPeliculasBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_peliculas, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()

        // Encuentra el RecyclerView dentro del layout del menú bottom de películas
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.rvNombre)

        adapter = AdapterNombrePeliculas(this@Register2Activity, listaPeliculas)
        recyclerView.layoutManager = LinearLayoutManager(this@Register2Activity)
        recyclerView.adapter = adapter

        // Obtiene las películas del ViewModel
        viewModel.obtenerTodasLasPaginas()
    }

    private fun showGenerosBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.menu_bottom_generos, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }
}