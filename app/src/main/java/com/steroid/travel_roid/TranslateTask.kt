package com.steroid.travel_roid

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.*
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

class TranslateTask(translationText:String) : AsyncTask<String, Void, String> (){
    var translationText = translationText
    override fun doInBackground(vararg p0: String?): String {
        val clientId = "Ge3xCYIlR7pE8p7yNiVe"
        val clientSercret = "cP885YoL58"

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

        fun connect(apiURL: String): HttpURLConnection {
            return try {
                val url = URL(apiURL)
                (url.openConnection() as HttpURLConnection)
            }catch (e: MalformedURLException){
                throw RuntimeException("API URL 오류", e)
            }catch (e: IOException) {
                throw RuntimeException("연결 실패",e)
            }
        }

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

        fun post(apiUrl: String, requestHeaders: Map<String, String>, text: String): String {
            val con: HttpURLConnection = connect(apiUrl)
            val postParams = "source=ko&target=en&text=$text"

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

        val responseBody: String = post(apiURL, requestHeaders, text)
        println("번역결과 : $responseBody")

        var parser: JsonParser = JsonParser()
        var element: JsonElement = parser.parse(responseBody)
        var data: String = ""

        if(element.asJsonObject.get("errorMessage") != null){
            Log.d("번역오류", "번역오류 발생 : ${element.asJsonObject.get("errorCode").toString()}")
            data = "A: 오류"
        }else if (element.asJsonObject.get("message") != null){
            data = element.asJsonObject.get("message").getAsJsonObject().get("result").asJsonObject.get("translatedText").asString

            Log.d("번역성공", "성공 : $data")
        }
        return data
    }
}