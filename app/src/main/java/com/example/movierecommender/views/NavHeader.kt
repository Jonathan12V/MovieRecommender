package com.example.movierecommender.views;

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.movierecommender.R

class NavHeader : AppCompatActivity(){

    private lateinit var tvUser: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.nav_header_main)

        tvUser = findViewById(R.id.nav_header_textView)

        tvUser.setText(UserInfo.username);
    }
}
