package com.example.home

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
            supportFragmentManager.beginTransaction().replace(R.id.home, HomeFragment()).commitAllowingStateLoss()
        }



    }

    public override fun onResume() {
        super.onResume()
    }


}