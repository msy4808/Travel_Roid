package com.steroid.travel_roid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import java.util.*

class MainActivity : AppCompatActivity() {
    var langCode = ""
    var autoLangCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userEnterText: EditText = findViewById(R.id.textIn)
        val btn: Button = findViewById<Button>(R.id.btn)
        val result: TextView = findViewById(R.id.result)
        val outSpinner: Spinner = findViewById(R.id.langTag) //spinner 메뉴

        //spinner 메뉴 어댑터 연결
        outSpinner.adapter = ArrayAdapter.createFromResource(this, R.array.langList, android.R.layout.simple_spinner_item)

        //spinner 리스너 설정
        outSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            //클릭했을때 언어코드 전달해줘야함
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        langCode = "ko"
                    }
                    1 -> {
                        langCode = "ja"
                    }
                    2 -> {
                        langCode = "zh-CN"
                    }
                    3 -> {
                        langCode = "zh-TW"
                    }
                    4 -> {
                        langCode = "hi"
                    }
                    5 -> {
                        langCode = "en"
                    }
                    6 -> {
                        langCode = "es"
                    }
                    7 -> {
                        langCode = "fr"
                    }
                    8 -> {
                        langCode = "de"
                    }
                    9 -> {
                        langCode = "pt"
                    }
                    10 -> {
                        langCode = "vi"
                    }
                    11 -> {
                        langCode = "id"
                    }
                    12 -> {
                        langCode = "fa"
                    }
                    13 -> {
                        langCode = "ar"
                    }
                    14 -> {
                        langCode = "mm"
                    }
                    15 -> {
                        langCode = "th"
                    }
                    16 -> {
                        langCode = "ru"
                    }
                    17 -> {
                        langCode = "it"
                    }

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

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
                        autoLangCode = detectLangs.getlangCode()
//                        outSpinner.setSelection() 선택된 spinner 변경하기
                    }
                },3000)
            }
        }

        btn.setOnClickListener {
            if(langCode == autoLangCode) {
                val translate = TranslateTask(userEnterText.text.toString(), langCode)
                result.text = translate.execute().get()
            }else{
                Toast.makeText(this, "현재 감지된 언어는 $autoLangCode 이고, 선택된 언어는 $langCode 입니다", Toast.LENGTH_SHORT).show()
                result.text = "선택된 언어를 확인해주세요"
            }
        }

        userEnterText.addTextChangedListener(textWatcher)

    }
}