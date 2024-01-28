package ipt.dam2324.tasktracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ipt.dam2324.tasktracker.model.NoteRequest
import ipt.dam2324.tasktracker.model.User
import ipt.dam2324.tasktracker.model.UserRequest
import ipt.dam2324.tasktracker.model.UserResponse
import ipt.dam2324.tasktracker.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Utilizadores : Fragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var listViewUsers: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_utilizadores, container, false)

        val sharedPreferences =
            requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        listViewUsers = view.findViewById(R.id.listViewUsers)

        // Retrieve usernames JSON string from SharedPreferences
        val usernamesJson = sharedPreferences.getString("users", "")

        // Convert JSON string to a list of usernames
        val usernamesList =
            Gson().fromJson<List<String>>(usernamesJson, object : TypeToken<List<String>>() {}.type)

        // Create an ArrayAdapter to display the usernames in the ListView
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, usernamesList)

        // Set the adapter for the ListView
        listViewUsers.adapter = adapter

        // Configurar o ouvinte de clique no ListView
        listViewUsers.setOnItemClickListener { _, _, position, _ ->
            onListItemClick(position)
        }

        swipeRefreshLayout.setOnRefreshListener {
            getUsers()
        }

        val buttonAddUser = view.findViewById<Button>(R.id.buttonAddUser)

        // Adicionar um clique ao botão para deletar o usuário (necessário obter o ID do usuário)
        buttonAddUser.setOnClickListener {
            Log.e("teste","O BOTAO FOI CLICADO")
            delUser(userId)
        }

        return view
    }

    // Método chamado quando um item da lista é clicado
    private fun onListItemClick(position: Int) {
        // Faça o que você precisa com a posição clicada
        Log.d("LIST_ITEM_CLICK", "Item clicado na posição: $position")
        // Obter o ID do usuário com base na posição clicada (precisa ser ajustado conforme necessário)
        userId = (position + 2).toString()
        Log.d("LIST_ITEM_CLICK", "userId= $userId")
    }

    // Método para recarregar os dados do fragmento
    private fun recarregarFragmento() {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Retrieve usernames JSON string from SharedPreferences
        val usernamesJson = sharedPreferences.getString("users", "")

        // Convert JSON string to a list of usernames
        val usernamesList =
            Gson().fromJson<List<String>>(usernamesJson, object : TypeToken<List<String>>() {}.type)
        Log.d("users", "$usernamesList")
        // Clear the existing data in the adapter
        adapter.clear()

        // Add the updated data to the adapter
        adapter.addAll(usernamesList)

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged()

        // Set the adapter for the ListView
        listViewUsers.adapter = adapter
        swipeRefreshLayout.isRefreshing = false
    }

    private fun getUsers() {
        val call = RetrofitInitializer().service().getUsers("Bearer Tostas")
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()?.users
                    val usersStrings = mutableListOf<String>()
                    if (users != null) {
                        for (user in users) {
                            val util = user.username
                            if (util != null) {
                                usersStrings.add(util)
                            }
                        }
                        val usernamesJson = Gson().toJson(usersStrings)
                        saveUsersToSharedPreferences(usernamesJson)
                        recarregarFragmento()
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("GET_USERS_ERROR", "Erro ao obter usuários: ${t.message}", t)
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun delUser(userId: String) {


        // Chamada para excluir o usuário da API
        val call = RetrofitInitializer().service().deleteUser("Bearer Tostas", userId)

        call.enqueue(object : Callback<UserRequest> {
            override fun onResponse(call: Call<UserRequest>, response: Response<UserRequest>) {
                if (response.isSuccessful) {
                    Log.d("DELETE_USER_SUCCESS", "Usuário removido com sucesso")
                    // Atualizar a lista após a exclusão bem-sucedida (opcional)
                    delNotes(userId)
                } else {
                    Log.e("DELETE_USER_ERROR", "Erro ao remover usuário: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserRequest>, t: Throwable) {
                Log.e("DELETE_USER_FAILURE", "Falha ao remover usuário: ${t.message}", t)
            }
        })
    }

    private fun delNotes(userId: String){
        // Chamada para excluir o usuário da API
        val call = RetrofitInitializer().service().deleteNotes("Bearer Tostas", userId)

        call.enqueue(object : Callback<NoteRequest> {
            override fun onResponse(call: Call<NoteRequest>, response: Response<NoteRequest>) {
                if (response.isSuccessful) {
                    Log.d("DELETE_USER_SUCCESS", "Usuário removido com sucesso")
                    // Atualizar a lista após a exclusão bem-sucedida (opcional)
                    getUsers()
                } else {
                    Log.e("DELETE_USER_ERROR", "Erro ao remover usuário: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NoteRequest>, t: Throwable) {
                Log.e("DELETE_USER_FAILURE", "Falha ao remover usuário: ${t.message}", t)
            }
        })

    }

    private fun saveUsersToSharedPreferences(usernamesJson: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("users", usernamesJson).apply()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Utilizadores()
    }
}
