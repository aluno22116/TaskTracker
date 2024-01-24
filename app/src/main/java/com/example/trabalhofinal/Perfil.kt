
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.trabalhofinal.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Perfil : Fragment(R.layout.fragment_perfil) {

    private val ourRequestCode = 123
    private val cameraPermissionRequest = 124
    private lateinit var currentPhotoPath: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCameraButton(view)

        val textViewNome = view.findViewById<TextView>(R.id.usernamePerfil)
        val textViewUsername = view.findViewById<TextView>(R.id.nomePerfil)
        val textViewEmail = view.findViewById<TextView>(R.id.emailPerfil)

        // Ao criar a visão, você pode acessar os dados do SharedPreferences
        val sharedPreferences = activity?.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val nome = sharedPreferences?.getString("name", "")
        val username = sharedPreferences?.getString("username", "")
        val email = sharedPreferences?.getString("email", "")

        // Definir textos nas TextViews
        textViewNome.text = "Nome: $nome"
        textViewUsername.text = "Username: $username"
        textViewEmail.text = "Email: $email"
    }

    // Função chamada quando o botão de tirar foto é clicado
    fun takePhoto() {
        // Verificar se a permissão da câmera foi concedida
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Se não, solicitar permissão
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                cameraPermissionRequest
            )
        } else {
            // Se a permissão foi concedida, iniciar a câmera
            iniciarCamera()
        }
    }

    // Função para iniciar a câmera
    private fun iniciarCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            val photoFile: File? = try {
                // Criar arquivo para armazenar a imagem
                criarArquivoImagem()
            } catch (ex: IOException) {
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.trabalhofinal.fileprovider",
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                // Iniciar a câmera usando o código de solicitação definido
                startActivityForResult(intent, ourRequestCode)
            }
        } else {
            Log.e("Camera", "Erro ao iniciar a câmera")
        }
    }

    // Função para criar um arquivo de imagem temporário
    private fun criarArquivoImagem(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    // Função chamada quando a atividade da câmera é concluída

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ourRequestCode && resultCode == Activity.RESULT_OK) {
            val imageView: ImageView = requireView().findViewById(R.id.fotografia)

            // Certificar-se de que a URI da foto está correta
            val photoUri: Uri = Uri.fromFile(File(currentPhotoPath))

            // Configurar a foto na ImageView
            imageView.setImageURI(photoUri)
        }
    }

    // Função chamada quando as permissões são solicitadas pelo usuário
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Verificar se a permissão da câmera foi concedida
        if (requestCode == cameraPermissionRequest && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Se sim, iniciar a câmera
            iniciarCamera()
        }
    }

    // Função para configurar o botão de câmera (chame essa função onde você configura a interface do fragmento)
    private fun setupCameraButton(view: View) {
        val cameraButton: Button = view.findViewById(R.id.btnfoto)
        cameraButton.setOnClickListener {
            takePhoto()
        }
    }
}
