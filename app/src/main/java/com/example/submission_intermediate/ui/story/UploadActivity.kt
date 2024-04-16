package com.example.submission_intermediate.ui.story

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.submission_intermediate.R
import com.example.submission_intermediate.databinding.ActivityUploadBinding
import com.example.submission_intermediate.ui.MainActivity
import com.example.submission_intermediate.ui.auth.dataStore
import com.example.submission_intermediate.ui.user.UserViewModel
import com.example.submission_intermediate.ui.user.ViewModelFactory
import com.example.submission_intermediate.uitls.UserPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class UploadActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUploadBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var file: File? = null
    private var lat: Double? = null
    private var lon: Double? = null
    private lateinit var finalFile: File
    private lateinit var token: String

    private val cameraPermission = Manifest.permission.CAMERA
    private val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private val courseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION


    private val uploadViewModel : UploadViewModel by lazy {
        ViewModelProvider(this)[UploadViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setViewModel()
        setAction()
    }

    private fun setViewModel() {
        val preferences = UserPreferences.getInstance(this.dataStore)
        val userViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[UserViewModel::class.java]

        userViewModel.getToken().observe(this){
            token = it
        }

        uploadViewModel.isLoading.observe(this){
            showLoading(it)
        }

        uploadViewModel.message.observe(this){msg ->
            uploadRes(msg)
        }
    }

    private fun uploadRes(msg: String) {
        showToast(msg)
        if (msg == "Story created successfully") {
            val intent = Intent(this@UploadActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun setAction() {
        with(binding){
            locationRadio.setOnClickListener{ getMyLocation() }
            btnGallery.setOnClickListener{ startGallery() }
            btnCamera.setOnClickListener{ startCamera() }
            btnUpload.setOnClickListener{ uploadImage() }
        }
    }

    private fun getMyLocation() {
        if (hasLocationPermission()) {
            try {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            this.lat = it.latitude
                            this.lon = it.longitude
                        } ?: showToast("Tidak dapat mendapatkan lokasi saat ini")
                    }
                    .addOnFailureListener { e ->
                        showToast("Gagal mendapatkan lokasi: ${e.message}")
                    }
            } catch (e: SecurityException) {
                e.printStackTrace()
                showToast("Tidak dapat mengakses lokasi karena izin tidak diberikan")
            }
        } else {
            callPermissionLocation()
        }
    }

    private fun uploadImage() {
        val desc = binding.descriptionEd.text.toString().trim()

        if (!isValidInput(file, desc)) {
            showToast("Gagal Upload")
            return
        } else {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val myFile = file as File

                    var compFile: File? = null
                    var compFileSize = myFile.length()

                    while (compFileSize > 1 * 1024 * 1024) {
                        compFile = withContext(Dispatchers.Default) {
                            Compressor.compress(applicationContext, myFile)
                        }
                        compFileSize = compFile.length()
                    }
                    finalFile = compFile ?: myFile
                }
                val requestImageFile =
                    finalFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    finalFile.name,
                    requestImageFile
                )

                val descPart = desc.toRequestBody("text/plain".toMediaType())

                val latPart = lat.toString().toRequestBody("text/plain".toMediaType())
                val lonPart = lon.toString().toRequestBody("text/plain".toMediaType())

                if (binding.locationRadio.isChecked){
                    uploadViewModel.uploadStory(imageMultipart, descPart, token, latPart, lonPart)
                }else{
                    uploadViewModel.uploadStory(imageMultipart, descPart, token)
                }
            }
        }
    }

    private var anyImg = false
    private lateinit var currentImgPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentImgPath)
            file = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            anyImg = true
            binding.previewImage.setImageBitmap(result)
            binding.descriptionEd.requestFocus()
        }
    }

    private fun startCamera() {
        if (hasCameraPermission()) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            createTempFileImage(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@UploadActivity,
                    "com.example.submission_intermediate",
                    it
                )
                currentImgPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        } else {
            callPermissionCamera()
        }
    }

    private fun startGallery() {
        if (hasStoragePermission()) {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val picker = Intent.createChooser(intent, getString(R.string.pick_picture))
            launcherIntentGallery.launch(picker)
        } else {
            callPermissionStorage()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)
            file = myFile
            binding.previewImage.setImageURI(selectedImg)
            binding.descriptionEd.requestFocus()
        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFileImage(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun createTempFileImage(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private val timeStamp: String = SimpleDateFormat(FORMAT, Locale.US
    ).format(System.currentTimeMillis())

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showToast(msg: String) {
        Toast.makeText(
            this@UploadActivity,
            msg,
            Toast.LENGTH_LONG).show()
    }

    private fun isValidInput(file: File?, desc: String): Boolean {
        if (file == null) {
            showToast(resources.getString(R.string.warningAddImage))
            return false
        }

        if (desc.isEmpty()) {
            binding.descriptionEd.error = resources.getString(R.string.warningAddDescription)
            return false
        }

        return true
    }

    private fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, storagePermission) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, cameraPermission) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun hasLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, locationPermission) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, courseLocationPermission) == PackageManager.PERMISSION_GRANTED)
    }


    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startGallery()
            } else {
                showToast("Storage permission denied")
            }
        }

    private fun callPermissionStorage() {
        requestStoragePermission.launch(storagePermission)
    }

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                getMyLocation()
            } else {
                showToast("Location permission denied")
            }
        }

    private fun callPermissionLocation() {
        requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                showToast("Camera permission denied")
            }
        }

    private fun callPermissionCamera() {
        requestCameraPermission.launch(cameraPermission)
    }


    companion object {
        const val FORMAT = "MMddyyyy"
    }
}