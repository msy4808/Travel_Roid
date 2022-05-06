package com.steroid.travel_roid

import android.app.Application
import android.content.Context

class MyApplication: Application() { //나중에 프래그먼트등을 사용할때 Context를 사용하기 위한 클래스
    init {
        instance = this  //MainApplication 인스턴스가 초기화될때 instance를 초기화해줌

    }

    companion object{
        lateinit var instance:MyApplication    //늦은 초기화를 위해 iateinit 선언

        fun getContext():Context {
            return instance.applicationContext
        }
    }
}