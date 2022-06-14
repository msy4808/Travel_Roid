package com.steroid.travel_roid

import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.*
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URLEncoder
class DetectLangs(userText:String) { //파파고 API 중 언어감지 레퍼런스 코드 작성

    val apiUrl = "https://openapi.naver.com/v1/papago/detectLangs"
    val requestHeaders:MutableMap<String, String> = HashMap()
    var query: String = userText

    init {
        query = try {
            URLEncoder.encode(userText, "UTF-8")
        }catch (e: UnsupportedEncodingException){
            throw RuntimeException("인코딩 실패", e)
        }

        requestHeaders["X-Naver-Client-Id"] = clientId
        requestHeaders["X-Naver-Client-Secret"] = clientSercret
    }

    fun post(apiUrl: String, requestHeaders: Map<String, String>, query: String): String { //해당 메서드가 실행되면 파라미터를 API주소로 보낸 후 결과값을 받아옴
        val con: HttpURLConnection = connect(apiUrl)
        val postParams = "query=$query"
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
    fun getlangCode(): String { //API 서버에 요청을 보낸 후 return 값(언어코드)을 받아오는 메서드
        val responseBody = post(apiUrl, requestHeaders, query)
        Log.d("언어감지", responseBody)

        var parser: JsonParser = JsonParser()
        var element: JsonElement = parser.parse(responseBody)
        var data: String = ""

        if(element.asJsonObject.get("errorMessage") != null){
            Log.d("언어감지 오류", "감지 발생 : ${element.asJsonObject.get("errorCode").toString()}")
        }else if (element.asJsonObject.get("langCode") != null){
            data = element.asJsonObject.get("langCode").asString

            Log.d("언어감지 성공", "성공 : $data")
        }
        return data
    }

}