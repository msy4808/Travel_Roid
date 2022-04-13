package com.steroid.travel_roid

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import org.w3c.dom.Text


import com.kakao.sdk.user.UserApiClient


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userEnterText: EditText = findViewById(R.id.textIn)
        val btn: Button = findViewById<Button>(R.id.btn)
        val result: TextView = findViewById(R.id.result)

        btn.setOnClickListener {
            val startApp = TranslateTask(userEnterText.text.toString())
            result.text = startApp.execute().get()
        }




    }
}