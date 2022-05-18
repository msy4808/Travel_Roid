package com.steroid.travel_roid


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.*

import android.os.Bundle

import android.view.*

import androidx.fragment.app.Fragment

import android.net.Uri
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private var imageCapture: ImageCapture? = null

    private lateinit var previewView:PreviewView
    private lateinit var frameLayoutPreview: FrameLayout
    private lateinit var imageViewPreview: ImageView
    private lateinit var cameraBtn: ImageButton

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var cameraAnimationListener: Animation.AnimationListener

    private var savedUri: Uri? = null

    object PermissionUtil {

        fun checkPermission(context: Context, permissionList: List<String>): Boolean {
            for (i: Int in permissionList.indices) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permissionList[i]
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    return false
                }
            }

            return true

        }

        fun requestPermission(activity: Activity, permissionList: List<String>){
            ActivityCompat.requestPermissions(activity, permissionList.toTypedArray(), 10)
        }

    }

    private fun permissionCheck() {

        var permissionList =
            listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!PermissionUtil.checkPermission((activity as MainActivity).applicationContext, permissionList)) {
            PermissionUtil.requestPermission((activity as MainActivity), permissionList)
        } else {
            openCamera()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_camera, container, false)
        previewView = view.findViewById(R.id.preview_View)
        frameLayoutPreview = view.findViewById(R.id.frame_Preview)
        imageViewPreview = view.findViewById(R.id.image_Preview)
        cameraBtn = view.findViewById(R.id.camera_Btn)
        permissionCheck()
        openCamera()
        cameraBtn.setOnClickListener {
            savePhoto()
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        return view
    }

    private fun getOutputDirectory(): File {
        val mediaDir = (activity as MainActivity).externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        val filesDir:File = File("")
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance((activity as MainActivity).applicationContext)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch (e: Exception) {
                Log.d("실패", "바인딩 실패 $e")
            }
        }, ContextCompat.getMainExecutor((activity as MainActivity).applicationContext))

    }

    private fun savePhoto() {
        imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yy-mm-dd", Locale.US).format(System.currentTimeMillis()) + ".png"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(
            outputOption,
            ContextCompat.getMainExecutor((activity as MainActivity).applicationContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    showCaptureImage()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    onBackPressed()
                }
            })
    }
    private fun setCameraAnimationListener() {
        cameraAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                showCaptureImage()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        }
    }
    private fun hideCaptureImage() {
        imageViewPreview.setImageURI(null)
        frameLayoutPreview.visibility = View.GONE

    }

    private fun showCaptureImage(): Boolean {
        if (frameLayoutPreview.visibility == View.GONE) {
            frameLayoutPreview.visibility = View.VISIBLE
            imageViewPreview.setImageURI(savedUri)
            return false
        }
        return true
    }
    fun onBackPressed() {
        if (showCaptureImage()) {
            hideCaptureImage()
        } else {
            onBackPressed()

        }
    }
}