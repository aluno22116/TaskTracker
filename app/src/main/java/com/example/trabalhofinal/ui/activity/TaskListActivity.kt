package com.example.trabalhofinal.ui.activity

import com.example.trabalhofinal.model.Task
import com.example.trabalhofinal.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskListActivity {
    fun getTasksAPI() {
        val call= RetrofitInitializer().taskService().getTasks()
        processaLista(call)
    }

    fun processaLista(call: Call<List<Task>>){
        call.enqueue(object : Callback<List<Task>> {

            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {

                val responseBody = response.body()
                responseBody?.let {
                    for (task in it) {
                        val titulo = task.titulo
                        val descricao = task.descricao
                        val dataFim = task.dataFim
                        val estado = task.estado
                    }
                }
            }
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {

                t.printStackTrace()
            }
        })
    }
}