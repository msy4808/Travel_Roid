package com.steroid.travel_roid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userEnterText: EditText = findViewById(R.id.textIn)
        val btn: Button = findViewById<Button>(R.id.btn)
        val result: TextView = findViewById(R.id.result)
        val textWatcher = object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val timer = Timer()
                timer.schedule(object: TimerTask(){
                    override fun run() {
                        val detectLangs = DetectLangs(p0.toString())
                        Log.d("TextWatcher 테스트", p0.toString())
                        detectLangs.getlangCode()
                    }
                },3000)
            }
        }

        btn.setOnClickListener {
            val translate = TranslateTask(userEnterText.text.toString())
            result.text = translate.execute().get()
        }

        userEnterText.addTextChangedListener(textWatcher)

    }
}