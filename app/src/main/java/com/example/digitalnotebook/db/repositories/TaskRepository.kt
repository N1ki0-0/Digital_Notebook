package com.example.digitalnotebook.db.repositories

import com.example.digitalnotebook.db.model.Task
import com.example.digitalnotebook.domain.Resource

interface TaskRepository {
    suspend fun addTask(title: String, body: String): Resource<Unit>
    suspend fun getAllTask(): Resource<List<Task>>
    suspend fun deleteTask(taskId: String): Resource<Unit>
    suspend fun updateTask(taskId: String, title: String, body: String): Resource<Unit>
}