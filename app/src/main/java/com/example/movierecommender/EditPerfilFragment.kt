package com.example.movierecommender

import AESEncryption
import UserInfo
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.movierecommender.repository.UserRepository
import java.io.ByteArrayOutputStream
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class EditPerfilFragment : Fragment() {

    lateinit var buttonGuardar: Button
    lateinit var buttonCancelar: Button
    lateinit var imageViewPerfil: ImageView

    private lateinit var userRepository: UserRepository

    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private val AES: AESEncryption = AESEncryption()

    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_editar_perfil1, container, false)

        etUser = view.findViewById(R.id.etUsuario)
        etPassword = view.findViewById(R.id.etContraseña)
        etEmail = view.findViewById(R.id.etEmail)
        imageViewPerfil = view.findViewById(R.id.etPerfil)

        buttonGuardar = view.findViewById(R.id.buttonSave)
        buttonCancelar = view.findViewById(R.id.btCancelar)

        userRepository = UserRepository(requireContext())

        etUser.setText(UserInfo.username)
        etEmail.setText((userRepository.verEmail()))

        buttonGuardar.setOnClickListener {
            val encryptedPassword = AES.encrypt(etPassword.text.toString());
            val username = etUser.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            userRepository.cambiarCampos(username, email, password, selectedImageUri?.toString())

            Toast.makeText(
                requireContext(),
                "Datos actualizados",
                Toast.LENGTH_LONG
            ).show()

            // Enviar un código de resultado a MenuActivity para indicar que la imagen de perfil ha cambiado
            requireActivity().setResult(Activity.RESULT_OK)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, VisualizacionPerfilFragment())
                .commit()
        }

        buttonCancelar.setOnClickListener {
            // Reemplazar el contenido del contenedor principal con LoginFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, VisualizacionPerfilFragment())
                .commit()
        }

        // Asignar un OnClickListener al ImageView para abrir la cámara
        imageViewPerfil.setOnClickListener {
            showImageSourceDialog()
        }

        return view
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun abridCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    // Método para manejar el resultado de la captura de imagen desde la cámara
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val resizedBitmap = resizeBitmap(imageBitmap, 200, 200)
                    selectedImageUri = getImageUri(requireContext(), resizedBitmap)
                    imageViewPerfil.setImageBitmap(resizedBitmap)
                    setCircularImage(resizedBitmap)
                }
                PICK_IMAGE_REQUEST -> {
                        selectedImageUri = data?.data
                        val imageBitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedImageUri
                        )
                        val resizedBitmap = resizeBitmap(imageBitmap, 200, 200)
                        imageViewPerfil.setImageBitmap(resizedBitmap)
                        setCircularImage(resizedBitmap)
                }
            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    // Método para convertir un Bitmap en una Uri
    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String? = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "Title",
            null
        )
        return Uri.parse(path ?: "")
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Tomar foto", "Elegir foto existente")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Subir foto de perfil")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> abridCamara()
                1 -> openFileChooser()
            }
        }
        builder.show()
    }

    private fun setCircularImage(bitmap: Bitmap) {
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        roundedBitmapDrawable.isCircular = true
        imageViewPerfil.setImageDrawable(roundedBitmapDrawable)
    }

}
