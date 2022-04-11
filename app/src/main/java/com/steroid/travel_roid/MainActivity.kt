package com.steroid.travel_roid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
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
        val targetSpinner: Spinner = findViewById(R.id.langTag2)

        val handler = object:Handler(Looper.getMainLooper()){ //감지된 언어코드로 Spinner를 변경해주기 위해 핸들러 사용
            override fun handleMessage(msg: Message) {
                when(msg.what){
                    0 -> {
                        outSpinner.setSelection(0)
                    }
                    1 -> {
                        outSpinner.setSelection(1)
                    }
                    2 -> {
                        outSpinner.setSelection(2)
                    }
                    3 -> {
                        outSpinner.setSelection(3)
                    }
                    4 -> {
                        outSpinner.setSelection(4)
                    }
                    5 -> {
                        outSpinner.setSelection(5)
                    }
                    6 -> {
                        outSpinner.setSelection(6)
                    }
                    7 -> {
                        outSpinner.setSelection(7)
                    }
                    8 -> {
                        outSpinner.setSelection(8)
                    }
                    9 -> {
                        outSpinner.setSelection(9)
                    }
                    10 -> {
                        outSpinner.setSelection(10)
                    }
                    11 -> {
                        outSpinner.setSelection(11)
                    }
                    12 -> {
                        outSpinner.setSelection(12)
                    }
                    13 -> {
                        outSpinner.setSelection(13)
                    }
                    14 -> {
                        outSpinner.setSelection(14)
                    }
                    15 -> {
                        outSpinner.setSelection(15)
                    }
                    16 -> {
                        outSpinner.setSelection(16)
                    }
                    17 -> {
                        outSpinner.setSelection(17)
                    }
                }
            }
        }


        //spinner 메뉴 어댑터 연결
        outSpinner.adapter = ArrayAdapter.createFromResource(this, R.array.langList, android.R.layout.simple_spinner_item)
        targetSpinner.adapter = ArrayAdapter.createFromResource(this, R.array.langList, android.R.layout.simple_spinner_item)

        //spinner 리스너 설정
        outSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { //감지가 아니라 직접 선택해도 전달되게 ... 우선 만들어놈
                when(position) {
                    0 -> {
                        autoLangCode = "ko"
                    }
                    1 -> {
                        autoLangCode = "ja"
                    }
                    2 -> {
                        autoLangCode = "zh-CN"
                    }
                    3 -> {
                        autoLangCode = "zh-TW"
                    }
                    4 -> {
                        autoLangCode = "hi"
                    }
                    5 -> {
                        autoLangCode = "en"
                    }
                    6 -> {
                        autoLangCode = "es"
                    }
                    7 -> {
                        autoLangCode = "fr"
                    }
                    8 -> {
                        autoLangCode = "de"
                    }
                    9 -> {
                        autoLangCode = "pt"
                    }
                    10 -> {
                        autoLangCode = "vi"
                    }
                    11 -> {
                        autoLangCode = "id"
                    }
                    12 -> {
                        autoLangCode = "fa"
                    }
                    13 -> {
                        autoLangCode = "ar"
                    }
                    14 -> {
                        autoLangCode = "mm"
                    }
                    15 -> {
                        autoLangCode = "th"
                    }
                    16 -> {
                        autoLangCode = "ru"
                    }
                    17 -> {
                        autoLangCode = "it"
                    }

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        targetSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            //Spinner에서 선택한 target 언어코드를 langCode에 저장하는 코드
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

            override fun afterTextChanged(p0: Editable?) { //텍스트가 입력되면 동작함 <- 입력된 텍스트를 String으로 받아 DetectLangs클래스로 넘겨줌
                val timer = Timer()
                timer.schedule(object: TimerTask(){
                    override fun run() {
                        val detectLangs = DetectLangs(p0.toString())
                        autoLangCode = detectLangs.getlangCode()
                        when(autoLangCode){ //감지된 언어코드에 따라 핸들러에 msg로 넘겨줌
                            "ko" -> {
                                handler.sendEmptyMessage(0)
                            }
                            "ja" -> {
                                handler.sendEmptyMessage(1)
                            }
                            "zh-CN" -> {
                                handler.sendEmptyMessage(2)
                            }
                            "zh-TW" -> {
                                handler.sendEmptyMessage(3)
                            }
                            "hi" -> {
                                handler.sendEmptyMessage(4)
                            }
                            "en" -> {
                                handler.sendEmptyMessage(5)
                            }
                            "es" -> {
                                handler.sendEmptyMessage(6)
                            }
                            "fr" -> {
                                handler.sendEmptyMessage(7)
                            }
                            "de" -> {
                                handler.sendEmptyMessage(8)
                            }
                            "pt" -> {
                                handler.sendEmptyMessage(9)
                            }
                            "vi" -> {
                                handler.sendEmptyMessage(10)
                            }
                            "id" -> {
                                handler.sendEmptyMessage(11)
                            }
                            "fa" -> {
                                handler.sendEmptyMessage(12)
                            }
                            "ar" -> {
                                handler.sendEmptyMessage(13)
                            }
                            "mm" -> {
                                handler.sendEmptyMessage(14)
                            }
                            "th" -> {
                                handler.sendEmptyMessage(15)
                            }
                            "ru" -> {
                                handler.sendEmptyMessage(16)
                            }
                            "it" -> {
                                handler.sendEmptyMessage(17)
                            }
                        }
                    }
                },3000)
            }
        }

        btn.setOnClickListener {
                val translate = TranslateTask(userEnterText.text.toString(), autoLangCode, langCode) //텍스트와 두가지의 언어코드를 파라미터로 보냄
                result.text = translate.execute().get()
        }

        userEnterText.addTextChangedListener(textWatcher) //editText에텍스트가 입력되면 동작하는 리스너를 연결
    }
}
