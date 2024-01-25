
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trabalhofinal.CriarNotas
import com.example.trabalhofinal.R
import com.example.trabalhofinal.databinding.FragmentBloconotasBinding
import com.example.trabalhofinal.model.Note
import com.example.trabalhofinal.model.NoteResponse
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [BlocoNotas.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlocoNotas : Fragment() {
    private lateinit var binding: FragmentBloconotasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Use o layout inflater apropriado para o fragmento BlocoNotas
        binding = FragmentBloconotasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtonListeners()


    }





    // Função para converter a lista de notas em uma string
    private fun notesToString(notes: List<Note>): String {
        // Converte a lista de notas em uma string separada por algum caractere, por exemplo, vírgula
        return notes.joinToString { it.toString() }
    }

    // Função para salvar a string na SharedPreferences
    private fun saveNotesToSharedPreferences(notesString: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("notes", notesString)
        editor.apply()
    }

    private fun getSavedUserId(): String {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }

    private fun handleMenuItemClick(itemId: Int): Boolean {
        // Implemente a lógica para lidar com os itens do menu de navegação, se necessário
        return true
    }

    private fun getSavedNotes(): String {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("notes", "") ?: ""
    }


    private fun abrirCriarNotas() {
        val balanceViewFragment = CriarNotas()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }

    private fun setupButtonListeners() {
        val btnIrCriarNotas = binding.btnCreateNoteFromView
        // Carrega as notas da SharedPreferences e exibe na TextView
        val notesString = getSavedNotes()
        binding.textViewNotes.text = notesString
        btnIrCriarNotas.setOnClickListener {
            abrirCriarNotas()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BlocoNotas()
    }
}
