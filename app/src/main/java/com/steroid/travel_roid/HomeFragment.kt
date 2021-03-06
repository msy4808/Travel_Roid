package com.steroid.travel_roid

import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*


class HomeFragment : Fragment() {

    //전역변수 지정
    var langCode = ""
    var autoLangCode = ""
    var response_Text = ""
    lateinit var userEnterText: EditText
    lateinit var result: TextView
    lateinit var outSpinner: Spinner
    lateinit var targetSpinner: Spinner
    lateinit var handler: Handler

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //변수를 초기화
        val view = inflater.inflate(R.layout.fragment_home, container, false) //view를 선언 후 view를 참조해서 ID값 매핑
        val copyIn: ImageButton = view.findViewById(R.id.input_copy)
        val copyResult: ImageButton = view.findViewById(R.id.result_copy)
        var tts: TextToSpeech? = null
        val ttsIn_Btn: ImageButton = view.findViewById(R.id.inText_TTS)
        val ttsResult_Btn: ImageButton = view.findViewById(R.id.resultText_TTS)
        val share_Btn: ImageButton = view.findViewById(R.id.share)
        val user_Btn: ImageView = view.findViewById(R.id.userMenu)
        userEnterText = view.findViewById(R.id.textIn)
        result = view.findViewById(R.id.result)
        outSpinner= view.findViewById(R.id.langTag) //spinner 메뉴
        targetSpinner= view.findViewById(R.id.langTag2)
        if(!(imageText == "")){ //사진에서 텍스트 추출한 내용이 있을경우 내용을 붙여줌
            userEnterText.setText(imageText) //InPut 텍스트를 사진에서 추출한 텍스트로 변경
            postText(userEnterText.text) // 해당 텍스트 번역 메서드로 저달
        }

        handler = object:Handler(Looper.getMainLooper()){ //감지된 언어코드로 Spinner를 변경해주기 위해 핸들러 사용
            override fun handleMessage(msg: Message) {
                when (msg.what) { //받아온 msg에 따라 Spinner 변경
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
                    99 -> {
                        result.text = response_Text
                    }
                }
            }
        }
        user_Btn.setOnClickListener { //마이페이지 버튼 이벤트
            val intent:Intent = Intent((activity as MainActivity).applicationContext, MypageActivity::class.java)
            startActivity(intent)
        }

        tts = TextToSpeech(context) { //TTS (텍스트 읽기 기능) 언어 세팅
            if(it == TextToSpeech.SUCCESS) {
                tts?.setLanguage(Locale.KOREAN) //사용언어 초기화
            }
        }
        ttsIn_Btn.setOnClickListener { //InPut 텍스트 TTS 버튼 이벤트
            tts.speak(userEnterText.text,TextToSpeech.QUEUE_FLUSH,null,null) //읽어줄 데이터(텍스트) 넘겨주기
        }
        ttsResult_Btn.setOnClickListener { //Result 텍스트 TTS 버튼 이벤트
            tts.speak(result.text,TextToSpeech.QUEUE_FLUSH,null,null) //읽어줄 데이터(텍스트) 넘겨주기
        }

        copyIn.setOnClickListener { //텍스트 클립보드에 복사기능
            val manager = (activity as MainActivity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("입력데이터",userEnterText.text)
            manager.setPrimaryClip(clip)
            Toast.makeText(context,"클립보드에 복사되었습니다",Toast.LENGTH_SHORT).show()
        }
        copyResult.setOnClickListener { //텍스트 클립보드에 복사기능
            val manager = (activity as MainActivity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("번역데이터",result.text)
            manager.setPrimaryClip(clip)
            Toast.makeText(context,"클립보드에 복사되었습니다",Toast.LENGTH_SHORT).show()
        }

        share_Btn.setOnClickListener { //공유버튼 이벤트 리스너 생성
            try {
                val sendText = "번역내용 공유하기"
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText)
                sendIntent.type = "text/plain"
                startActivity(Intent.createChooser(sendIntent, "Share"))
            } catch (ignored: ActivityNotFoundException) {
                Toast.makeText((activity as MainActivity).applicationContext, "공유 에러", Toast.LENGTH_SHORT).show()
            }
        }

        //spinner 메뉴 어댑터 연결
        outSpinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.langList, android.R.layout.simple_spinner_item)
        targetSpinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.target_langList, android.R.layout.simple_spinner_item)

        targetSpinner.setSelection(1) //번역할 언어 2번째인 영어로 기본세팅
        //spinner 리스너 설정
        outSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) { //감지가 아니라 직접 선택해도 전달되게 ... 우선 만들어놈
                when(position) { //Spinner에서 선택된 메뉴에 따라 언어코드값을 저장
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
            //spinner 리스너 설정
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) { //Spinner에서 선택한 target 언어코드를 langCode에 저장하는 코드
                    0 -> {
                        langCode = "ko"
                    }
                    1 -> {
                        langCode = "en"
                    }
                    2 -> {
                        langCode = "ja"
                    }
                    3 -> {
                        langCode = "zh-TW"
                    }
                    4 -> {
                        langCode = "zh-CN"
                    }
                    5 -> {

                        langCode = "vi"
                    }
                    6 -> {
                        langCode = "id"
                    }
                    7 -> {
                        langCode = "th"
                    }
                    8 -> {
                        langCode = "de"
                    }
                    9 -> {
                        langCode = "es"
                    }
                    10 -> {
                        langCode = "fr"
                    }
                    11 -> {
                        langCode = "ru"
                    }
                    12 -> {
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
                postText(p0) //번약 메서드 실행
            }
        }
        userEnterText.addTextChangedListener(textWatcher) //editText에텍스트가 입력되면 동작하는 리스너를 연결
        return view
    }
    private fun postText(text: Editable?){ //텍스트를 받아서 TranslateTask 클래스로 넘겨줌(거기서 번역 실행)
        val timer = Timer()
        timer.schedule(object: TimerTask(){
            override fun run() {
                val detectLangs = DetectLangs(text.toString()) //입력된 텍스트를 전달해 언어코드 감지
                autoLangCode = detectLangs.getlangCode() //감지된 언어코드를 저장
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
                val translate = TranslateTask(userEnterText.text.toString(), autoLangCode, langCode) //텍스트와 두가지의 언어코드를 파라미터로 보냄
                response_Text = translate.execute().get() //결과값 저장
                handler.sendEmptyMessage(99)
            }
        },1000)
    }
}