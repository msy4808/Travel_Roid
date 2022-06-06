package com.steroid.travel_roid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class MypageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
        val user_Email: TextView = findViewById(R.id.user_Email)
        val user_Name: TextView = findViewById(R.id.user_name)
        val back_Btn: ImageButton = findViewById(R.id.mypage_Back_Btn)
        user_Email.text = email
        user_Name.text = "이름 : $name"

        back_Btn.setOnClickListener {
            finish()
        }
    }
}