package com.example.movierecommender.repository

import UserInfo
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.movierecommender.models.PeliculaModel
import com.google.gson.Gson

class UserRepository(context: Context) :
    SQLiteOpenHelper(context, "movie_recommender.db", null, 4) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement =
            "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, user VARCHAR(20), email VARCHAR(50), password VARCHAR(30), profile_image_path TEXT, peliculas_favoritas TEXT)";
        if (db != null) {
            db.execSQL(createTableStatement)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS user")
            onCreate(db)
        }
    }

    fun addUser(user: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("user", user)
        cv.put("email", email)
        cv.put("password", password)
        cv.put("profile_image_path", "perfil.png")
        val gson = Gson()
        val movieListJson = gson.toJson(emptyList<PeliculaModel>())
        cv.put("peliculas_favoritas", movieListJson)

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

    fun cambiarCampos(user: String, email: String, password: String, profileImagePath: String?) {
        val db = this.writableDatabase

        val updateFields = mutableListOf<String>()

        if (!password.isEmpty()) {
            val updatePasswordQuery = "UPDATE user SET password=? WHERE user=?"
            db.execSQL(updatePasswordQuery, arrayOf(password, UserInfo.username))
            updateFields.add("password")
        }

        if (!user.isEmpty()) {
            val updateUserQuery = "UPDATE user SET user=? WHERE user=?"
            db.execSQL(updateUserQuery, arrayOf(user, UserInfo.username))
            UserInfo.username = user
            updateFields.add("user")
        }

        if (!email.isEmpty()) {
            val updateEmailQuery = "UPDATE user SET email=? WHERE user=?"
            db.execSQL(updateEmailQuery, arrayOf(email, UserInfo.username))
            updateFields.add("email")
        }

        // Update profile image path if provided
        if (!profileImagePath.isNullOrEmpty()) {
            val updateImagePathQuery = "UPDATE user SET profile_image_path=? WHERE user=?"
            db.execSQL(updateImagePathQuery, arrayOf(profileImagePath, UserInfo.username))
            updateFields.add("profile_image_path")
        }

        // Log the updated fields
        if (updateFields.isNotEmpty()) {
            val updatedFieldsStr = updateFields.joinToString(", ")
            println("Updated fields: $updatedFieldsStr")
        }
    }

    @SuppressLint("Range")
    fun obtenerRutaImagenPerfil(): String? {
        val db = this.readableDatabase
        var profileImagePath: String? = null

        val query = "SELECT profile_image_path FROM user WHERE user=?"
        val cursor = db.rawQuery(query, arrayOf(UserInfo.username))

        if (cursor.moveToFirst()) {
            profileImagePath = cursor.getString(cursor.getColumnIndex("profile_image_path"))
        }
        cursor.close()
        db.close()

        return profileImagePath
    }
    @SuppressLint("Range")
    fun verEmail(): String {
        val db = this.readableDatabase
        var email = ""

        val query = "SELECT email FROM user WHERE user=?"
        val cursor = db.rawQuery(query, arrayOf(UserInfo.username))

        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndex("email"))
        }

        cursor.close()
        db.close()

        return email
    }

    @SuppressLint("Range")
    fun getIdUser(user: String): Long {
        val db = this.readableDatabase
        val query = "SELECT id FROM USER WHERE user=?"

        val cursor = db.rawQuery(query, arrayOf(user))

        var userId: Long = -1

        if (cursor.moveToFirst()) {
            // Si hay al menos una fila, obtenemos el valor del ID
            userId = cursor.getLong(cursor.getColumnIndex("id"))
        }

        // Cerramos el cursor después de usarlo
        cursor.close()

        return userId
    }


    //Metodos para ver que el usuario o el email no este ya en la base de datos
    fun checkUserAndEmailUsado(user: String, email: String): Int {
        val db = this.readableDatabase

        val userQuery = "SELECT * FROM user WHERE user = ?"
        val userEmailQuery = "SELECT * FROM user WHERE email = ?"

        val userCursor = db.rawQuery(userQuery, arrayOf(user))
        val userEmailCursor = db.rawQuery(userEmailQuery, arrayOf(email))

        val userExists = userCursor.count > 0
        val userEmailExists = userEmailCursor.count > 0

        userCursor.close()
        userEmailCursor.close()

        return when {
            userExists && userEmailExists -> 3 // Ambos están usados
            userExists -> 1 // Usuario está usado
            userEmailExists -> 2 // Email está usado
            else -> -1 // Ninguno está usado
        }
    }

    fun checkPasswordSecurity(password: String): Boolean {
        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{6,}$")
        return regex.matches(password)
    }

    fun checkEmailEstructura(email: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return regex.matches(email)
    }
}

