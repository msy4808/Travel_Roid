package com.steroid.travel_roid

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

import java.util.*





class WeatherFragment : Fragment() {





    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체

    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10




    lateinit var text1: TextView
    lateinit var text2: TextView
    lateinit var text3: TextView
    lateinit var geocoder: Geocoder

    lateinit var tvWind : TextView     // 풍속
    lateinit var tvWind05 : TextView
    lateinit var tvWind08 : TextView
    lateinit var tvWind11 : TextView
    lateinit var tvWind14 : TextView
    lateinit var tvWind17 : TextView
    lateinit var tvWind20: TextView
    lateinit var tvWind23 : TextView

    lateinit var tvHumidity : TextView      // 강수 형태
    lateinit var tvHumidity05 : TextView
    lateinit var tvHumidity08 : TextView
    lateinit var tvHumidity11 : TextView
    lateinit var tvHumidity14 : TextView
    lateinit var tvHumidity17 : TextView
    lateinit var tvHumidity20 : TextView
    lateinit var tvHumidity23 : TextView

    lateinit var tvHumidityNo : TextView      // 습도
    lateinit var tvSky : TextView           // 하늘 상태
    lateinit var tvTemp : TextView          // 온도
    lateinit var tvTemp02 : TextView
    lateinit var tvTemp05 : TextView
    lateinit var tvTemp08 : TextView
    lateinit var tvTemp11 : TextView
    lateinit var tvTemp14 : TextView
    lateinit var tvTemp17 : TextView
    lateinit var tvTemp20 : TextView
    lateinit var tvTemp23 : TextView
    lateinit var tvTempHi : TextView        //최고온도
    lateinit var tvTempLo : TextView        //최저온도
    lateinit var btnRefresh : Button        // 새로고침 버튼
    lateinit var weather:ImageView          //날씨그림
    lateinit var weather02:ImageView
    lateinit var weather05:ImageView
    lateinit var weather08:ImageView
    lateinit var weather11:ImageView
    lateinit var weather14:ImageView
    lateinit var weather17:ImageView
    lateinit var weather20:ImageView
    lateinit var weather23:ImageView

    lateinit var tempDress: TextView
    lateinit var dress:TextView

    private fun startLocationUpdates() {

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        Looper.myLooper()?.let {
            mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
                it
            )
        }
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        val address = geocoder.getFromLocation(mLastLocation.latitude,mLastLocation.longitude,100)

        //text1.text = "위도 : " + mLastLocation.latitude // 갱신 된 위도
        //text2.text = "경도 : " + mLastLocation.longitude // 갱신 된 경도
        text3.text =  address.get(0).locality
        //" "+address.get(0).subLocality+" "+address.get(0).thoroughfare

    }


    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }



    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this.requireContext(), "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }








    }

    companion object{

        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "3e167cb3d121a8385cf010efb97d336f"
        var lat = "37.4033679"
        var lon = "126.9297888"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var weatherview = inflater.inflate(R.layout.fragment_weather, container, false)


        text1 = weatherview.findViewById(R.id.text1)
        text2 = weatherview.findViewById(R.id.text2)
        text3 = weatherview.findViewById(R.id.text3)
        tempDress = weatherview.findViewById(R.id.tempDress)
        dress = weatherview.findViewById(R.id.dress)

        tvWind = weatherview.findViewById(R.id.tvWind)
        tvWind05 = weatherview.findViewById(R.id.tvWind05)
        tvWind08 = weatherview.findViewById(R.id.tvWind08)
        tvWind11 = weatherview.findViewById(R.id.tvWind11)
        tvWind14 = weatherview.findViewById(R.id.tvWind14)
        tvWind17 = weatherview.findViewById(R.id.tvWind17)
        tvWind20 = weatherview.findViewById(R.id.tvWind20)
        tvWind23 = weatherview.findViewById(R.id.tvWind23)

        tvHumidityNo = weatherview.findViewById(R.id.tvHumidityNo)

        tvHumidity = weatherview.findViewById(R.id.tvHumidity)
        tvHumidity05 = weatherview.findViewById(R.id.tvHumidity05)
        tvHumidity08 = weatherview.findViewById(R.id.tvHumidity08)
        tvHumidity11 = weatherview.findViewById(R.id.tvHumidity11)
        tvHumidity14 = weatherview.findViewById(R.id.tvHumidity14)
        tvHumidity17 = weatherview.findViewById(R.id.tvHumidity17)
        tvHumidity20 = weatherview.findViewById(R.id.tvHumidity20)
        tvHumidity23 = weatherview.findViewById(R.id.tvHumidity23)

        tvSky = weatherview.findViewById(R.id.tvSky)
        tvTemp = weatherview.findViewById(R.id.tvTemp)
        tvTemp02 = weatherview.findViewById(R.id.tvTemp02)
        tvTemp05 = weatherview.findViewById(R.id.tvTemp05)
        tvTemp08 = weatherview.findViewById(R.id.tvTemp08)
        tvTemp11 = weatherview.findViewById(R.id.tvTemp11)
        tvTemp14 = weatherview.findViewById(R.id.tvTemp14)
        tvTemp17 = weatherview.findViewById(R.id.tvTemp17)
        tvTemp20 = weatherview.findViewById(R.id.tvTemp20)
        tvTemp23 = weatherview.findViewById(R.id.tvTemp23)
        tvTempHi = weatherview.findViewById(R.id.tvTempHi)
        tvTempLo = weatherview.findViewById(R.id.tvTempLo)

        weather = weatherview.findViewById(R.id.weather)
        weather02 = weatherview.findViewById(R.id.weather02)
        weather05 = weatherview.findViewById(R.id.weather05)
        weather08 = weatherview.findViewById(R.id.weather08)
        weather11 = weatherview.findViewById(R.id.weather11)
        weather14 = weatherview.findViewById(R.id.weather14)
        weather17 = weatherview.findViewById(R.id.weather17)
        weather20 = weatherview.findViewById(R.id.weather20)
        weather23 = weatherview.findViewById(R.id.weather23)



        geocoder = Geocoder(this.requireContext())

        mLocationRequest =  LocationRequest.create().apply {

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }

        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)


        val call = service.getCurrentWeatherData(lat, lon, AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("TEST", "result :" + t.message)
            }

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {

                if(response.code() == 200){
                    val weatherResponse = response.body()
                    Log.d("MainActivity", "result: " + weatherResponse.toString())
                    var cTemp =  weatherResponse!!.main!!.temp - 273.15  //켈빈을 섭씨로 변환
                    var minTemp = weatherResponse!!.main!!.temp_min - 273.15
                    var maxTemp = weatherResponse!!.main!!.temp_max - 273.15
                    var humidity =weatherResponse!!.main!!.humidity
                    var sky= weatherResponse!!.weather!!.get(0).main
                    var wind = weatherResponse!!.wind!!.speed


                    tvTemp.text = cTemp.toInt().toString() + "°"
                    tvTempHi.text = "최고"+maxTemp.toInt().toString()+ "°  "
                    tvTempLo.text = "최저"+minTemp.toInt().toString()+ "°"



                    tvTemp02.text = "  "+cTemp.toInt().toString() + "°"
                    tvTemp05.text = "  "+cTemp.toInt().toString() + "°"
                    tvTemp08.text = "  "+cTemp.toInt().toString() + "°"
                    tvTemp11.text = "  "+cTemp.toInt().toString() + "°"
                    tvTemp14.text = "  "+cTemp.toInt().toString() + "°"
                    tvTemp17.text = "  "+cTemp.toInt().toString() + "°"
                    tvTemp20.text = "  "+cTemp.toInt().toString() + "°"
                    tvTemp23.text = "  "+cTemp.toInt().toString() + "°"

                    tvHumidity.text = humidity.toInt().toString()+"%"
                    tvHumidity05.text = humidity.toInt().toString()+"%"
                    tvHumidity08.text = humidity.toInt().toString()+"%"
                    tvHumidity11.text = humidity.toInt().toString()+"%"
                    tvHumidity14.text = humidity.toInt().toString()+"%"
                    tvHumidity17.text = humidity.toInt().toString()+"%"
                    tvHumidity20.text = humidity.toInt().toString()+"%"
                    tvHumidity23.text = humidity.toInt().toString()+"%"

                    tvWind.text = wind.toString()+"m/s  "
                    tvWind05.text = wind.toString()+"m/s  "
                    tvWind08.text = wind.toString()+"m/s  "
                    tvWind11.text = wind.toString()+"m/s  "
                    tvWind14.text = wind.toString()+"m/s  "
                    tvWind17.text = wind.toString()+"m/s  "
                    tvWind20.text = wind.toString()+"m/s  "
                    tvWind23.text = wind.toString()+"m/s  "

                    tvSky.text = sky



                    when(cTemp.toInt().toString()) {
                        in "5".."8" -> dress.text ="울 코트, 가죽 옷, 기모"
                        in "9".."11" -> dress.text ="트렌치 코트, 야상, 점퍼"
                        in "12".."16" -> dress.text ="자켓, 가디건, 청자켓"
                        in "17".."19" -> dress.text ="니트, 맨투맨, 후드, 긴바지"
                        in "20".."22" -> dress.text ="블라우스, 긴팔 티, 슬랙스"
                        in "23".."27" -> dress.text ="얇은 셔츠, 반바지, 면바지"
                        in "28".."50" -> dress.text ="민소매, 반바지, 린넨 옷"
                        else -> dress.text ="패딩, 누빔 옷, 목도리"
                    }
                    when(cTemp.toInt().toString()) {
                        in "5".."8" -> tempDress.text =" 5° ~ 8° "
                        in "9".."11" -> tempDress.text =" 9° ~ 11° "
                        in "12".."16" -> tempDress.text =" 12° ~ 16° "
                        in "17".."19" -> tempDress.text =" 17° ~ 19° "
                        in "20".."22" -> tempDress.text =" 20° ~ 22° "
                        in "23".."27" -> tempDress.text =" 23° ~ 27° "
                        in "28".."50" -> tempDress.text =" 28° ~ 35° "
                        else -> tempDress.text =" 5°이하 "
                    }

                    when(sky) {
                        "1" ->  weather.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather.setImageResource(R.drawable.cloud)
                        "4" ->  weather.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }
                    when(sky) {
                        "1" ->  weather02.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather02.setImageResource(R.drawable.cloud)
                        "4" ->  weather02.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }
                    when(sky) {
                        "1" ->  weather05.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather05.setImageResource(R.drawable.cloud)
                        "4" ->  weather05.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }
                    when(sky) {
                        "1" ->  weather08.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather08.setImageResource(R.drawable.cloud)
                        "4" ->  weather08.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }
                    when(sky) {
                        "1" ->  weather11.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather11.setImageResource(R.drawable.cloud)
                        "4" ->  weather11.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }
                    when(sky) {
                        "1" ->  weather14.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather14.setImageResource(R.drawable.cloud)
                        "4" ->  weather14.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }
                    when(sky) {
                        "1" ->  weather17.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather17.setImageResource(R.drawable.cloud)
                        "4" ->  weather17.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }
                    when(sky) {
                        "1" ->  weather20.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather20.setImageResource(R.drawable.cloud)
                        "4" ->  weather20.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }
                    when(sky) {
                        "1" ->  weather23.setImageResource(R.drawable.sun)
                        "Clouds" ->  weather23.setImageResource(R.drawable.cloud)
                        "4" ->  weather23.setImageResource(R.drawable.cloud)
                        else -> weather.setImageResource(R.drawable.cloud)
                    }


                }
            }

        })



        startLocationUpdates()



        return weatherview


    }






}

interface WeatherService{

    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String) :
            Call<WeatherResponse>
}

class WeatherResponse(){
    @SerializedName("weather") var weather = ArrayList<Weather>()
    @SerializedName("main") var main: Main? = null
    @SerializedName("wind") var wind : Wind? = null
    @SerializedName("sys") var sys: Sys? = null
}

class Weather {
    @SerializedName("id") var id: Int = 0
    @SerializedName("main") var main : String? = null
    @SerializedName("description") var description: String? = null
    @SerializedName("icon") var icon : String? = null
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
    @SerializedName("humidity")
    var humidity: Float = 0.toFloat()
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
    @SerializedName("temp_min")
    var temp_min: Float = 0.toFloat()
    @SerializedName("temp_max")
    var temp_max: Float = 0.toFloat()

}

class Wind {
    @SerializedName("speed")
    var speed: Float = 0.toFloat()
    @SerializedName("deg")
    var deg: Float = 0.toFloat()
}

class Sys {
    @SerializedName("country")
    var country: String? = null
    @SerializedName("sunrise")
    var sunrise: Long = 0
    @SerializedName("sunset")
    var sunset: Long = 0
}
