package com.steroid.travel_roid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.kakao.sdk.common.util.Utility


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var keyHash = Utility.getKeyHash(this)
        Log.d("키해시", keyHash)
        var bottomNavi = findViewById<BottomNavigationView>(R.id.bottomNavi)
        supportFragmentManager.beginTransaction().replace(R.id.home_ly, HomeFragment()).commit()
        bottomNavi.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.home_ly, HomeFragment()).commit()
                   true
                }
                R.id.camera -> {
                    supportFragmentManager.beginTransaction().replace(R.id.home_ly, CameraFragment()).commit()
                    true
                }
                R.id.weather -> {
                    supportFragmentManager.beginTransaction().replace(R.id.home_ly, WeatherFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
}

