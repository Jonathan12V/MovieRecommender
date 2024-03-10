package com.example.movierecommender

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import com.example.movierecommender.repository.UserRepository
import java.io.FileNotFoundException

class VisualizacionPerfilFragment : Fragment() {

    private lateinit var tvUser: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUser2: TextView
    private lateinit var imageViewPerfil: ImageView
    private lateinit var userRepository: UserRepository

    // Código de solicitud de permiso (puede ser cualquier número)
    private val REQUEST_EXTERNAL_STORAGE_PERMISSION = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_visualizar_perfil, container, false)

        tvUser = rootView.findViewById(R.id.tvNombrePerfil)
        tvEmail = rootView.findViewById(R.id.tvEmail)
        tvUser2 = rootView.findViewById(R.id.tvUser2)
        imageViewPerfil = rootView.findViewById(R.id.imageViewPerfil)

        userRepository = UserRepository(requireContext())

        val profileImagePath = userRepository.obtenerRutaImagenPerfil()

        if (!isStoragePermissionGranted()) {
            requestStoragePermission()
        } else {
            loadProfileImage(profileImagePath, imageViewPerfil)
        }

        val btnEditarPerfil = rootView.findViewById<Button>(R.id.btnEditarPerfil)

        tvUser.text = UserInfo.username
        tvUser2.text = UserInfo.username
        tvEmail.text = userRepository.verEmail()

        btnEditarPerfil.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditPerfilFragment())
                .commit()
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        // Actualizar la imagen de perfil cada vez que se muestre la actividad
        actualizarImagenPerfil()
    }

    private fun actualizarImagenPerfil() {
        val profileImagePath = userRepository.obtenerRutaImagenPerfil()
        loadProfileImage(profileImagePath, imageViewPerfil)
    }
    private fun loadProfileImage(profileImagePath: String?, imageView: ImageView) {
        if (!profileImagePath.isNullOrEmpty()) {
            try {
                val imageUri = Uri.parse(profileImagePath)
                val bitmap =
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        imageUri
                    )
                val roundedBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(resources, bitmap)
                roundedBitmapDrawable.isCircular = true
                imageView.setImageDrawable(roundedBitmapDrawable)
            } catch (e: FileNotFoundException) {
                // Manejar la excepción si la imagen no se encuentra
                // Cargar la imagen por defecto en caso de que la ruta no sea válida
                val profileImageResourceId = R.mipmap.perfil
                val bitmap = BitmapFactory.decodeResource(resources, profileImageResourceId)
                val roundedBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(resources, bitmap)
                roundedBitmapDrawable.isCircular = true
                imageView.setImageDrawable(roundedBitmapDrawable)
            }
        } else {
            // Si la ruta de la imagen es nula o vacía, cargar la imagen por defecto
            val profileImageResourceId = R.mipmap.perfil
            val bitmap = BitmapFactory.decodeResource(resources, profileImageResourceId)
            val roundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(resources, bitmap)
            roundedBitmapDrawable.isCircular = true
            imageView.setImageDrawable(roundedBitmapDrawable)
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_EXTERNAL_STORAGE_PERMISSION
        )
    }

    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, carga la imagen del perfil
                val profileImagePath = userRepository.obtenerRutaImagenPerfil()
                loadProfileImage(profileImagePath, imageViewPerfil)
            } else {
                // Permiso denegado, maneja el caso según tu lógica de la aplicación
            }
        }
    }
}

