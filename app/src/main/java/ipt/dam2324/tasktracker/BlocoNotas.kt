
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ipt.dam2324.tasktracker.CriarNotas
import ipt.dam2324.tasktracker.R
import ipt.dam2324.tasktracker.databinding.FragmentBloconotasBinding

/**
 * Uma [Fragment] simples.
 * Utilize o método de fábrica [BlocoNotas.newInstance]
 * para criar uma instância deste fragmento.
 */
class BlocoNotas : Fragment() {
    private lateinit var binding: FragmentBloconotasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Utiliza o inflater de layout apropriado para o fragmento BlocoNotas
        binding = FragmentBloconotasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configura os ouvintes dos botões
        setupButtonListeners()
    }

    // Obtém as notas guardadas em formato de string das SharedPreferences
    private fun getSavedNotes(): String {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("notes", "") ?: ""
    }

    // Abre a tela de criação de notas (CriarNotas)
    private fun abrirCriarNotas() {
        val balanceViewFragment = CriarNotas()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, balanceViewFragment)
            .commit()
    }

    // Configura os ouvintes dos botões
    private fun setupButtonListeners() {
        val btnIrCriarNotas = binding.btnCreateNoteFromView
        // Carrega as notas das SharedPreferences e exibe na TextView
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
