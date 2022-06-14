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
        var keyHash = Utility.getKeyHash(this) //카카오에서 지원하는 메소드로 해시키 구하기
        Log.d("키해시", keyHash)
        var bottomNavi = findViewById<BottomNavigationView>(R.id.bottomNavi)
        supportFragmentManager.beginTransaction().replace(R.id.home_ly, HomeFragment()).commit() //초기화면 홈 프래그먼트로 지정
        bottomNavi.setOnNavigationItemSelectedListener { item -> //바텀네비 메뉴를 클릭하면 해당하는 프래그먼트로 전환
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

