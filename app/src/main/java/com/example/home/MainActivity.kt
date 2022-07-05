package com.example.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.findFragment
import com.example.home.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()

    }



    private fun initClickListener() {
        binding.toolBar.ivMenu.setOnClickListener {             //menu 클릭 시 open drawer
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        val header : View = binding.navigationView.getHeaderView(0)
        val image : ImageView = header.findViewById(R.id.iv_close)
        image.setOnClickListener {                          // X 클릭 시 close drawer
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

    }



}