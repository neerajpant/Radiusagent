package com.example.radiusagent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.radiusagent.databinding.ActivityMainBinding
import com.example.radiusagent.ui.FacilitesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(binding.frame.id, FacilitesFragment(), "productFragment")
        ft.commit()


    }

}