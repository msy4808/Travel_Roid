package com.steroid.travel_roid

import android.app.Application
import com.kakao.sdk.common.KakaoSdk



class GlobalApplication:Application() {
    override fun onCreate(){
        super.onCreate()
        KakaoSdk.init(this,"d3bc84d744e74ec2f19755c8cc701ad5")
    }

}