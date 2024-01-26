import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

import ipt.dam2324.tasktracker.R
import ipt.dam2324.tasktracker.databinding.FragmentPerfilBinding
import ipt.dam2324.tasktracker.model.Note
import ipt.dam2324.tasktracker.model.NoteRequest
import ipt.dam2324.tasktracker.model.NoteResponse
import ipt.dam2324.tasktracker.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class Perfil : Fragment(R.layout.fragment_perfil) {

    private lateinit var binding: FragmentPerfilBinding
    private val ourRequestCode = 123
    private val cameraPermissionRequest = 124
    private lateinit var currentPhotoPath: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar o SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        setupCameraButton(view)

        val textViewNome = view.findViewById<TextView>(R.id.usernamePerfil)
        val textViewUsername = view.findViewById<TextView>(R.id.nomePerfil)
        val textViewEmail = view.findViewById<TextView>(R.id.emailPerfil)

        val userId = getSavedUserId()

        getImage(userId)

        // Verificar se há um caminho de foto salvo
        val savedPhotoPath = sharedPreferences.getString("photoPath", null)
        if (savedPhotoPath != null) {
            val imageView: ImageView = requireView().findViewById(R.id.fotografia)
            val photoUri: Uri = Uri.fromFile(File(savedPhotoPath))
            imageView.setImageURI(photoUri)
        }

        // Ao criar a visão, você pode acessar os dados do SharedPreferences
        val nome = sharedPreferences.getString("name", "")
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("email", "")

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
                // Adicionar userId ao nome do arquivo para distinguir entre usuários
                criarArquivoImagem()
            } catch (ex: IOException) {
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "ipt.dam2324.tasktracker.fileprovider",
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
        val userId = getSavedUserId()
        val fileName = "JPEG_${userId}_${timeStamp}_"

        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            fileName,
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

            // Salvar o caminho da foto no SharedPreferences se o userId for o mesmo
            val userId = getSavedUserId()
            val savedUserId = sharedPreferences.getString("userId", "")
            if (userId == savedUserId) {
                val editor = sharedPreferences.edit()
                editor.putString("photoPath", currentPhotoPath)
                editor.apply()
                imagem(userId)
            } else {
                // Limpar o caminho da foto anterior
                Log.e("AQUI VEEEEEEE", "AQUUIIIIIIIIIIIII")
                val editor = sharedPreferences.edit()
                editor.remove("photoPath")
                editor.apply()

                // Salvar o novo userId e o caminho da foto
                editor.putString("userId", userId)
                editor.putString("photoPath", currentPhotoPath)
                editor.apply()
            }
        }
    }

    // Função para configurar o botão de câmera (chame essa função onde você configura a interface do fragmento)
    private fun setupCameraButton(view: View) {
        val cameraButton: Button = view.findViewById(R.id.btnfoto)
        cameraButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun imagem(userId: String) {
        val imagem = currentPhotoPath
        val nota = Note(null, null, null, imagem)
        val notes = NoteRequest(nota)
        val putCall = RetrofitInitializer().service().updateNote("Bearer Tostas", userId, notes)
        putCall.enqueue(object : Callback<NoteRequest> {
            override fun onResponse(call: Call<NoteRequest>, response: Response<NoteRequest>) {
                if (response.isSuccessful) {
                    // Trate a resposta bem-sucedida conforme necessário
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

    private fun getImage(userId: String) {
        // Verifica se o userId é válido
        if (userId.isNotEmpty()) {
            val call = RetrofitInitializer().service().getNotes("Bearer Tostas", userId)

            call.enqueue(object : Callback<NoteResponse> {
                override fun onResponse(call: Call<NoteResponse>, response: Response<NoteResponse>) {
                    if (response.isSuccessful) {
                        val noteResponse = response.body()
                        if (noteResponse != null) {
                            val notes = noteResponse.notes
                            val imagemStrings = mutableListOf<String>()

                            if (notes != null && notes.isNotEmpty()) {
                                // O usuário tem notas, faça algo com as notas obtidas da API
                                // Por exemplo, exibir ou processar as notas
                                Log.e("Notas", "O usuário tem notas")
                                Log.i("INFO", "Usuário encontrado: $notes")

                                // Itera sobre a lista de notas e obtém o parâmetro 'notas'
                                for (note in notes) {
                                    // Verifica se o ID do usuário da nota é o mesmo que o usuário logado
                                    if (note.id.toString() == userId) {
                                        val nota = note.imagem
                                        if (nota != null) {
                                            imagemStrings.add(nota)
                                        }
                                    }
                                }

                                // Converte a lista de strings em uma string separada por algum caractere, por exemplo, vírgula
                                val imgString = imagemStrings.joinToString()

                                if (imgString != null) {
                                    val imageView: ImageView = requireView().findViewById(R.id.fotografia)
                                    val photoUri: Uri = Uri.fromFile(File(imgString))
                                    imageView.setImageURI(photoUri)
                                } else {
                                    // O usuário não tem notas
                                    Log.e("Notas", "O usuário não tem notas")
                                    // Aqui você pode decidir o que fazer quando o usuário não tem notas
                                    // Por exemplo, exibir uma mensagem ou realizar alguma outra ação
                                }
                            } else {
                                Log.e("Erro", "Resposta nula.")
                            }
                        }
                    } else {
                        Log.e("Erro", "Erro na chamada à API: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("Erro", "Erro na chamada à API: ${t.message}")
                }
            })
        } else {
            // userId não é válido, faça algo aqui se necessário
            Log.e("Erro", "userId inválido")
        }
    }

    private fun getSavedUserId(): String {
        val savedUserId = sharedPreferences.getString("userId", "")
        return if (savedUserId.isNullOrEmpty()) {
            // Gerar um novo ID se nenhum estiver salvo
            val newUserId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString("userId", newUserId).apply()
            newUserId
        } else {
            savedUserId
        }
    }

    private fun saveNotesToSharedPreferences(notesString: String) {
        val editor = sharedPreferences.edit()
        editor.putString("notes", notesString)
        editor.apply()
    }
}
