package com.example.movierecommender.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.movierecommender.models.PeliculaModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieRepository(context: Context) : SQLiteOpenHelper(context, "movie_recommender.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase?) {
        // No es necesario crear una nueva tabla para las peliculas
        // ya que ahora se almacena las peliculas favoritas
        // directamente en el campo "peliculas_favoritas" de la tabla "user".
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
    @SuppressLint("Range")
    fun movieExists(userId: Int, movieId: Int): Boolean {
        val db = this.readableDatabase
        val query = "SELECT peliculas_favoritas FROM user WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        var exists = false

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val favoritesJson = cursor.getString(cursor.getColumnIndex("peliculas_favoritas"))
                val favoritesList = convertJsonStringToList(favoritesJson)

                exists = favoritesList.any { it["id"]?.toString()?.toDoubleOrNull() == movieId.toDouble() }
            } else {
                Log.d("MovieRepository", "Cursor is empty for userId: $userId")
            }
            cursor.close()
        } else {
            Log.d("MovieRepository", "Cursor is null for userId: $userId")
        }

        return exists
    }
    @SuppressLint("Range")
    fun deleteMovie(userId: Int, movieId: Int): Int {
        val db = this.writableDatabase
        db.beginTransaction()

        var deletedRows = 0

        try {445
            // Obtener los datos actuales del usuario
            val getUserQuery = "SELECT * FROM user WHERE id = ?"
            val cursor = db.rawQuery(getUserQuery, arrayOf(userId.toString()))

            if (cursor != null && cursor.moveToFirst()) {
                val currentFavorites = cursor.getString(cursor.getColumnIndex("peliculas_favoritas"))

                // Convertir la cadena JSON a una lista de objetos
                val currentFavoritesList = convertJsonStringToList(currentFavorites)

                // Log para imprimir el ID de la película que se intenta eliminar
                Log.d("MovieRepository", "Deleting movie with ID: $movieId")
                // Verificar si la película está presente en la lista antes de intentar eliminarla
                if (currentFavoritesList.any { (it["id"] as Double).toInt() == movieId}) {
                    // Log para verificar si la condición es verdadera antes de la eliminación
                    Log.d("MovieRepository", "Movie found in favorites: ${currentFavoritesList.any { (it["id"] as Double).toInt() == movieId }}")
                    // Eliminar la película de la lista por el ID
                    val iterator = currentFavoritesList.iterator()
                    while (iterator.hasNext()) {
                        val movie = iterator.next()
                        if ((movie["id"] as Double).toInt() == movieId) {
                            iterator.remove()
                            deletedRows++
                            break
                        }
                    }

                    // Convertir la lista de nuevo a una cadena JSON
                    val newFavoritesJson = convertListToJsonString(currentFavoritesList)

                    // Actualizar el campo peliculas_favoritas en la tabla user
                    val updateUserFavoritesQuery = "UPDATE user SET peliculas_favoritas = ? WHERE id = ?"
                    db.execSQL(updateUserFavoritesQuery, arrayOf(newFavoritesJson, userId))
                } else {
                    Log.d("MovieRepository", "Movie with ID $movieId not found in favorites list for user ID $userId")
                }
            }

            cursor?.close()
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        return deletedRows
    }

    @SuppressLint("Range")
    fun addMovie(movieId: Int, movieName: String, posterUrl: String, userId: Int): Long {
        val db = this.writableDatabase

        // Obtener los datos actuales del usuario
        val getUserQuery = "SELECT * FROM user WHERE id = ?"
        val cursor = db.rawQuery(getUserQuery, arrayOf(userId.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            val currentFavorites = cursor.getString(cursor.getColumnIndex("peliculas_favoritas"))

            // Convertir la cadena JSON a una lista de objetos
            val currentFavoritesList = convertJsonStringToList(currentFavorites)

            // Verificar si la película ya está en la lista antes de agregarla
            val isMovieAlreadyAdded = currentFavoritesList.any { it["id"] == movieId }
            if (!isMovieAlreadyAdded) {
                // Crear un nuevo objeto JSON para la nueva película
                val newMovie = mapOf("id" to movieId, "nombrePelicula" to movieName, "poster" to posterUrl)

                // Agregar la nueva película a la lista existente
                currentFavoritesList.add(0, newMovie)

                // Convertir la lista de nuevo a una cadena JSON
                val newFavoritesJson = convertListToJsonString(currentFavoritesList)

                // Actualizar el campo peliculas_favoritas en la tabla user
                val updateUserFavoritesQuery = "UPDATE user SET peliculas_favoritas = ? WHERE id = ?"
                db.execSQL(updateUserFavoritesQuery, arrayOf(newFavoritesJson, userId))
            }
        }

        cursor?.close()
        return movieId.toLong()
    }

    private fun convertJsonStringToList(jsonString: String): MutableList<Map<String, Any>> {
        return Gson().fromJson(jsonString, object : TypeToken<MutableList<Map<String, Any>>>() {}.type)
            ?: mutableListOf()
    }

    private fun convertListToJsonString(list: List<Map<String, Any>>): String {
        return Gson().toJson(list)
    }

    @SuppressLint("Range")
    fun getMovies(userId: Int): List<PeliculaModel> {
        val movies = mutableListOf<PeliculaModel>()
        val db = this.readableDatabase
        val query = "SELECT peliculas_favoritas FROM user WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor != null && cursor.moveToFirst()) {
            val favoritesJson = cursor.getString(cursor.getColumnIndex("peliculas_favoritas"))
            val favoriteMoviesList = convertJsonStringToList(favoritesJson)

            for (favoriteMovie in favoriteMoviesList) {
                val movieId = (favoriteMovie["id"] as Double).toInt()
                val movieName = favoriteMovie["nombrePelicula"] as String
                val posterUrl = favoriteMovie["poster"] as String

                val movie = PeliculaModel(movieId, movieName, "", posterUrl, "", "", "", emptyList())
                movies.add(movie)
            }
        }
        cursor?.close()
        return movies
    }

}
