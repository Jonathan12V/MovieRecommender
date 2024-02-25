// AdapterPeliculas.kt
package com.example.movierecommender.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.movierecommender.R
import com.example.movierecommender.models.PeliculaModel
import java.util.HashSet

class AdapterNombrePeliculas(
    val context: Context,
    var listaPeliculas: List<PeliculaModel>
): RecyclerView.Adapter<AdapterNombrePeliculas.ViewHolder>() {

    private val selectedPositions = HashSet<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_nombre_pelicula, parent, false)
        return ViewHolder(vista, selectedPositions)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelicula = listaPeliculas[position]
        holder.bind(pelicula, selectedPositions, context)
    }
    class ViewHolder(itemView: View, private val selectedPositions: HashSet<Int>): RecyclerView.ViewHolder(itemView) {
        val tvMovieName = itemView.findViewById(R.id.tv_name_movie) as TextView
        val radioButton = itemView.findViewById(R.id.radio_button_movie) as RadioButton

        fun bind(pelicula: PeliculaModel, selectedPositions: HashSet<Int>, context: Context) {
            tvMovieName.text = pelicula.nombrePelicula
            radioButton.isChecked = selectedPositions.contains(adapterPosition)

            radioButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (selectedPositions.contains(position)) {
                        selectedPositions.remove(position)
                        radioButton.isChecked = false
                    } else {
                        if (selectedPositions.size < MAX_SELECTION) {
                            selectedPositions.add(position)
                            radioButton.isChecked = true
                        } else {
                            // Muestra un mensaje de error si excede el límite de selección
                            Toast.makeText(context, "Solo puedes seleccionar hasta $MAX_SELECTION películas", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listaPeliculas.size
    }

    fun actualizarLista(nuevaLista: List<PeliculaModel>) {
        listaPeliculas = nuevaLista
        notifyDataSetChanged()
    }
    companion object {
        const val MAX_SELECTION = 10
    }
}


