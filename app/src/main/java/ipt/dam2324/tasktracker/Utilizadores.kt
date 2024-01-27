package ipt.dam2324.tasktracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ipt.dam2324.tasktracker.model.UserResponse
import ipt.dam2324.tasktracker.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Utilizadores : Fragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var listViewUsers: ListView
    private lateinit var adapter: ArrayAdapter<String>

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

        swipeRefreshLayout.setOnRefreshListener {
            getUsers()
        }

        return view
    }

    // Método para recarregar os dados do fragmento
    private fun recarregarFragmento() {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Retrieve usernames JSON string from SharedPreferences
        val usernamesJson = sharedPreferences.getString("users", "")

        // Convert JSON string to a list of usernames
        val usernamesList = Gson().fromJson<List<String>>(usernamesJson, object : TypeToken<List<String>>() {}.type)
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

                        // Itera sobre a lista de notas e obtém o parâmetro 'notas'
                        for (user in users) {

                            val util = user.username
                            if (util != null) {
                                usersStrings.add(util)
                            }
                        }

                        val usernamesJson = Gson().toJson(usersStrings)

                        // Converte a lista de strings em uma string separada por algum caractere, por exemplo, vírgula
                        val usersStrings = usersStrings.joinToString()

                        // Salva a string na SharedPreferences
                        saveUsersToSharedPreferences(usernamesJson)
                        recarregarFragmento()
                    }

                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Tratar falhas na chamada à API (pode ser necessário notificar o usuário)
                Log.e("GET_USERS_ERROR", "Erro ao obter usuários: ${t.message}", t)
                swipeRefreshLayout.isRefreshing = false
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
