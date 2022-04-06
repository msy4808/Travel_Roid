package com.steroid.travel_roid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

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