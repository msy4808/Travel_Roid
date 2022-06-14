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

import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.core.*
import androidx.camera.view.CameraController

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions

var imageText:String = ""

class CameraFragment : Fragment() { //카메라 X를 이용한 클래스

    private var imageCapture: ImageCapture? = null

    private lateinit var previewView: PreviewView
    private lateinit var frameLayoutPreview: FrameLayout
    private lateinit var imageViewPreview: ImageView
    private lateinit var cameraBtn: ImageButton

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var camera: Camera
    private lateinit var cameraController:CameraControl
    private lateinit var cameraInfo: CameraInfo

    private var savedUri: Uri? = null

    //텍스트 추출관련 - 각 언어별 베타 지원(현재 한국어 언어팩 설정)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val chineserecognizer = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
    val devanagerrecognizer = TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())
    val japneserecognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
    val koreanrecognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    private lateinit var image: InputImage

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

        fun requestPermission(activity: Activity, permissionList: List<String>) {
            ActivityCompat.requestPermissions(activity, permissionList.toTypedArray(), 10)
        }

    }

    private fun permissionCheck() {

        var permissionList =
            listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!PermissionUtil.checkPermission(
                (activity as MainActivity).applicationContext,
                permissionList
            )
        ) {
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
        //변수값을 초기화 시켜줌
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        val flash_Btn: ImageButton = view.findViewById(R.id.flash_Btn)
        previewView = view.findViewById(R.id.preview_View)
        frameLayoutPreview = view.findViewById(R.id.frame_Preview)
        imageViewPreview = view.findViewById(R.id.image_Preview)
        cameraBtn = view.findViewById(R.id.camera_Btn)
        permissionCheck()
        openCamera()
        flash_Btn.setOnClickListener {
            when (cameraInfo.torchState.value) {
                TorchState.ON -> {
                    cameraController.enableTorch(false)
                }
                TorchState.OFF -> {
                    cameraController.enableTorch(true)
                }
            }
        }
        cameraBtn.setOnClickListener {
            savePhoto()
        }

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        return view
    }

    private fun getOutputDirectory(): File { //저장소 연결
        val mediaDir = (activity as MainActivity).externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        val filesDir: File = File("")
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun openCamera() { //카메라 실행 메서드
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance((activity as MainActivity).applicationContext)

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
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                cameraController = camera.cameraControl
                cameraInfo = camera.cameraInfo

            } catch (e: Exception) {
                Log.d("실패", "바인딩 실패 $e")
            }
        }, ContextCompat.getMainExecutor((activity as MainActivity).applicationContext))

    }

    private fun savePhoto() { //사진 저장 메서드
        imageCapture = imageCapture ?: return

        val photoFile = File( //저장 형식(폼) 지정
            outputDirectory,
            SimpleDateFormat("yy-mm-dd", Locale.US).format(System.currentTimeMillis()) + ".png"
        )
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture?.takePicture(
            outputOption,
            ContextCompat.getMainExecutor((activity as MainActivity).applicationContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)//저장된 사진의 uri를 이용해 사진의 uri값을 저장
                    showCaptureImage()
                    runTextRecognition()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    onBackPressed()
                }
            })
    }


    private fun runTextRecognition() { //저장된 사진을 불러와서 텍스트가 있다면 텍스트 추출 메서드로 넘겨줌
        image = InputImage.fromFilePath((activity as MainActivity).applicationContext, savedUri!!)
        koreanrecognizer.process(image)
            .addOnSuccessListener(
                OnSuccessListener<Text?> { texts ->
                    processTextRecognitionResult(texts)
                })
            .addOnFailureListener(
                OnFailureListener { e -> // Task failed with an exception
                    e.printStackTrace()
                })
    }

    private fun processTextRecognitionResult(texts: Text) { //텍스트를 추출하는 메서드
        val blocks: List<Text.TextBlock> = texts.getTextBlocks()
        Log.d("텍스트", "${blocks}")
        if (blocks.size == 0) {
            Toast.makeText(context,"No text found",Toast.LENGTH_SHORT).show()
            return
        }
        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].getLines()
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].getElements()
                for(k in elements.indices) {
                    imageText += elements.get(k).text
                    imageText += " "
                    Log.d("텍스트", "${elements.get(k).text}")
                }
            }
        }
        Log.d("최종", imageText)

        (activity as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.home_ly, HomeFragment()).commit()
    }

    private fun hideCaptureImage() {
        imageViewPreview.setImageURI(null)
        frameLayoutPreview.visibility = View.GONE

    }

    private fun showCaptureImage(): Boolean { // 촬영한 사진을 화면에 보여줌
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