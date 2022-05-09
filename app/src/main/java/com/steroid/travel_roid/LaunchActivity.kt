package com.steroid.travel_roid

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        TedPermission.with(this).setPermissionListener(object : PermissionListener{
            override fun onPermissionGranted() {
                startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
                finish()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                for(i in deniedPermissions!!)
                    Toast.makeText(applicationContext, "Test",Toast.LENGTH_SHORT).show()
            }
        }).setDeniedMessage("앱을 정상적으로 실행하려면 권한이 필요합니다.").setPermissions(Manifest.permission.CAMERA).check()
    }
}