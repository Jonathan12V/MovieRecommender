package com.example.movierecommender.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserRepository(context: Context) :
    SQLiteOpenHelper(context, "movie_recommender.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement =
            "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, user VARCHAR(20), email VARCHAR(50), password VARCHAR(30))";
        if (db != null) {
            db.execSQL(createTableStatement)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addUser(user: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("user", user)
        cv.put("email", email)
        cv.put("password", password)

        return db.insert("user", null, cv)
    }

    @SuppressLint("Range")
    fun checkUser(user: String, password: String): Int {
        val db = this.readableDatabase
        val query = "SELECT * FROM user WHERE user = ?"
        val cursor = db.rawQuery(query, arrayOf(user))

        val userExists = cursor.count > 0
        if (userExists) {
            cursor.moveToFirst()
            val storedPassword = cursor.getString(cursor.getColumnIndex("password"))
            cursor.close()

            return if (storedPassword == password) {
                1 // Usuario y contraseña coinciden
            } else {
                3 // Contraseña incorrecta para el usuario dado
            }
        } else {
            cursor.close()
            return 2 // No se encontró ningún usuario con ese nombre
        }
    }


}