package com.example.movierecommender.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.movierecommender.models.PeliculaModel

class MovieRepository(context: Context) : SQLiteOpenHelper(context, "movie_recommender.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("MovieRepository", "onCreate called")
        val createTableStatement =
            "CREATE TABLE movie (id INTEGER PRIMARY KEY AUTOINCREMENT, movie_id INTEGER, movie_name VARCHAR(100), poster_url VARCHAR(100))";
        db?.execSQL(createTableStatement)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
    fun movieExists(movieId: Int): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM movie WHERE movie_id = $movieId"
        val cursor = db.rawQuery(query, null)

        val exists = cursor != null && cursor.count > 0
        cursor?.close()
        return exists
    }

    fun deleteMovie(movieId: Int): Int {
        val db = this.writableDatabase
        return db.delete("movie", "movie_id = ?", arrayOf(movieId.toString()))
    }
    fun addMovie(movieId: Int, movieName: String, posterUrl: String): Long {
        val db = this.writableDatabase

        if (movieExists(movieId)) {
            deleteMovie(movieId)
        }

        val cv = ContentValues()

        cv.put("movie_id", movieId)
        cv.put("movie_name", movieName)
        cv.put("poster_url", posterUrl)

        return db.insert("movie", null, cv)

    }

    @SuppressLint("Range")
    fun getMovies(): List<PeliculaModel> {
        val movies = mutableListOf<PeliculaModel>()
        val db = this.readableDatabase
        val query = "SELECT * FROM movie"
        val cursor = db.rawQuery(query, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val movieId = cursor.getInt(cursor.getColumnIndex("movie_id"))
                val movieName = cursor.getString(cursor.getColumnIndex("movie_name"))
                val posterUrl = cursor.getString(cursor.getColumnIndex("poster_url"))

                val movie = PeliculaModel(movieId, movieName, "", posterUrl, "", "", "", emptyList());
                movies.add(movie)
            } while (cursor.moveToNext())
        }

        cursor?.close()
        return movies
    }
}
