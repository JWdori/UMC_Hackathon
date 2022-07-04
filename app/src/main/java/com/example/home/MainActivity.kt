package com.example.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.home.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        initSetOnClickListener()


    }

    private fun initSetOnClickListener(){

        binding.toolBar.ivMenu.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }



    }

    public override fun onResume() {
        super.onResume()
    }


}