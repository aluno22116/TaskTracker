package com.example.trabalhofinal

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.trabalhofinal.databinding.ActivityPerfilBinding
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Base64
import android.widget.EditText
import com.example.trabalhofinal.model.Note
import com.example.trabalhofinal.model.NoteRequest
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Perfil : TesteMenu() {
    private lateinit var binding: ActivityPerfilBinding
    private val ourRequestCode = 123
    private val cameraPermissionRequest = 124
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleMenuItemClick(menuItem.itemId)
            true
        }
    }

    fun takePhoto(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraPermissionRequest
            )
        } else {
            iniciarCamera()
        }
    }

    private fun iniciarCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            val photoFile: File? = try {
                criarArquivoImagem()
            } catch (ex: IOException) {
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.trabalhofinal.fileprovider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                startActivityForResult(intent, ourRequestCode)
            }
        } else {
            Log.e("Camera", "Erro ao iniciar a câmera")
        }
    }

    private fun criarArquivoImagem(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ourRequestCode && resultCode == RESULT_OK) {
            val imageView: ImageView = findViewById(R.id.fotografia)

            // Certifique-se de que a URI da foto está correta
            val photoUri = Uri.fromFile(File(currentPhotoPath))

            // Configurar a foto na ImageView
            imageView.setImageURI(photoUri)

            // Get the userId (replace "yourUserId" with the actual method to retrieve userId)
            val userId = getSavedUserId()

            // Call the updateNotes function to send the image to the API
            updateNotes(userId, currentPhotoPath)
        }
    }

    private fun updateNotes(userId: String, imageFilePath: String) {
        val imageView: ImageView = findViewById(R.id.fotografia)

        // Reduzir o tamanho da imagem antes de converter para Base64
        val compressedImageFile = compressImage(imageFilePath)
        val base64Image = imageToBase64(compressedImageFile)

        val nota = Note(null, null, null, base64Image)
        val notes = NoteRequest(nota)

        val putCall = RetrofitInitializer().service().updateNote("Bearer Tostas", userId, notes)

        putCall.enqueue(object : Callback<NoteRequest> {
            override fun onResponse(call: Call<NoteRequest>, response: Response<NoteRequest>) {
                if (response.isSuccessful) {
                    Log.i("ALERTA!!!!!!!!", "Nota colocada na API")
                } else {
                    Log.e("Erro", "Erro na chamada à API de atualização de nota: ${response.message()}")
                    // Lide com o erro conforme necessário
                }
            }

            override fun onFailure(call: Call<NoteRequest>, t: Throwable) {
                t.printStackTrace()
                Log.e("Erro", "Erro na chamada à API de atualização de nota: ${t.message}")
                // Lide com o erro conforme necessário
            }
        })
    }

    private fun compressImage(imageFilePath: String): File {
        val originalImageFile = File(imageFilePath)

        val compressedImageFile = id.zelory.compressor.Compressor(this)
            .setMaxWidth(640) // Largura máxima permitida da imagem
            .setMaxHeight(480) // Altura máxima permitida da imagem
            .setQuality(75) // Qualidade da imagem comprimida (0 - 100)
            .setCompressFormat(Bitmap.CompressFormat.JPEG) // Formato da imagem comprimida
            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).absolutePath) // Diretório de destino da imagem comprimida
            .compressToFile(originalImageFile)

        return compressedImageFile
    }




    private fun imageToBase64(imageFile: File): String {
        val bytes = imageFile.readBytes()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun getSavedUserId(): String {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }
}
