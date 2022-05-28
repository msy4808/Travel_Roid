package com.steroid.travel_roid

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// 결과 xml 파일에 접근해서 정보 가져오기
interface WeatherInterface {
    // getVilageFcst : 단기 예보 조회
    @GET("getVilageFcst?serviceKey=AxCmgNjLNDJNcQki7sL0Kta8Y0%2Fx%2Fq5L8n9dBY%2BUJb3hjD0xaqV3lOZQkTXc3UvLxxkU0czKGBUqnY3eAYr7vQ%3D%3D")

    fun GetWeather(@Query("dataType") data_type : String,
                   @Query("numOfRows") num_of_rows : Int,
                   @Query("pageNo") page_no : Int,
                   @Query("base_date") base_date : String,
                   @Query("base_time") base_time : String,
                   @Query("nx") nx : String,
                   @Query("ny") ny : String)
            : Call<WEATHER>
}