package com.example.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.home.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()

    }

    private fun init(){
        binding.ivClose.setOnClickListener { finish() }
    }

}