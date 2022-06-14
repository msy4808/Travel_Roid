package com.steroid.travel_roid

import android.os.AsyncTask
import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.*
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

val clientId = "Ge3xCYIlR7pE8p7yNiVe"
val clientSercret = "cP885YoL58"

fun connect(apiURL: String): HttpURLConnection { //두가지의 클래스(DetectLangs 와 TranslateTask 에서 공통적으로 사용하는 메서드기에 최상위함수로 작성)
    return try {
        val url = URL(apiURL)
        (url.openConnection() as HttpURLConnection)
    }catch (e: MalformedURLException){
        throw RuntimeException("API URL 오류", e)
    }catch (e: IOException) {
        throw RuntimeException("연결 실패",e)
    }
}

//파파고 번역 API 레퍼런스 코드로 작성
class TranslateTask(translationText:String, langCode: String, targetLangCode: String) : AsyncTask<String, Void, String> (){
    var langCode = langCode
    var targetLangCode = targetLangCode
    var translationText = translationText
    override fun doInBackground(vararg p0: String?): String {
        val apiURL = "https://openapi.naver.com/v1/papago/n2mt"
        var text: String = translationText
        text = try {
            URLEncoder.encode(translationText, "UTF-8")
        }catch (e: UnsupportedEncodingException) {
            throw RuntimeException("인코딩 실패", e)
        }
        val requestHeaders: MutableMap<String, String> = HashMap()
        requestHeaders["X-Naver-Client-Id"] = clientId
        requestHeaders["X-Naver-Client-Secret"] = clientSercret



        fun readBody(body: InputStream): String {
            val streamReader = InputStreamReader(body);
            try{
                BufferedReader(streamReader).use { lineReader ->
                    val responseBody = StringBuilder()
                    var line: String?
                    while (lineReader.readLine().also { line = it } != null) {
                        responseBody.append(line)
                    }
                    return responseBody.toString()
                }
            }catch (e: IOException){
                throw RuntimeException("API 응답 리드 실패", e)
            }
        }

        fun post(apiUrl: String, requestHeaders: Map<String, String>, text: String): String { //API 서버에 요청을 보내는 메서드
            val con: HttpURLConnection = connect(apiUrl)
            val postParams = "source=$langCode&target=$targetLangCode&text=$text"
            println("번역 코드 테스트 ; $postParams")
            try{
                con.requestMethod = "POST"

                for (header: Map.Entry<String, String> in requestHeaders.entries){
                    con.setRequestProperty(header.key, header.value)
                }
                con.doOutput = true
                DataOutputStream(con.outputStream).use { wr ->
                    wr.write(postParams.toByteArray())
                    wr.flush()
                }
                val responseCode = con.responseCode
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    return readBody(con.inputStream)
                }else{
                    return readBody(con.errorStream)
                }
            }catch (e: IOException){
                throw RuntimeException("API 요청과 응답 실패", e)
            } finally {
                con.disconnect()
            }
        }

        val responseBody: String = post(apiURL, requestHeaders, text) //responseBody란 변수에 API 서버의 요청결과를 저장
        println("번역결과 : $responseBody")

        //JSON 타입을 다루기 위해 GSON 라이브러리 사용
        var parser: JsonParser = JsonParser()
        var element: JsonElement = parser.parse(responseBody)
        var data: String = ""

        if(element.asJsonObject.get("errorMessage") != null){ //오류일 경우 출력
            Log.d("번역오류", "번역오류 발생 : ${element.asJsonObject.get("errorCode").toString()}")
            data = "A: 오류"
        }else if (element.asJsonObject.get("message") != null){ //응답을 제대로 받을 경우 실행
            //JSON 파일을 Key값을 하나하나 파고 들어가 번역된 텍스트를 String값으로 저장
            data = element.asJsonObject.get("message").getAsJsonObject().get("result").asJsonObject.get("translatedText").asString

            Log.d("번역성공", "성공 : $data")
        }
        return data //번역된 결과값을 리턴
    }
}