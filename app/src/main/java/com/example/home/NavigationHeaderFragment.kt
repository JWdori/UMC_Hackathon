package com.example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.home.databinding.FragmentHomeBinding
import com.example.home.databinding.FragmentNavigationHeaderBinding

class NavigationHeaderFragment : Fragment() {

    lateinit var binding:FragmentNavigationHeaderBinding

//    interface MyItemClickListener{
//        fun onClick(view: View)
//    }
//
//    private lateinit var mItemClickListener: MyItemClickListener
//
//    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
//        mItemClickListener = itemClickListener
//    }
//
//    fun onClick(view : View){
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNavigationHeaderBinding.inflate(inflater, container, false)


        initClickListener()

        return binding.root

    }

    private fun initClickListener(){
//        binding.ivClose.setOnClickListener { mItemClickListener.onClick() }

    }


}