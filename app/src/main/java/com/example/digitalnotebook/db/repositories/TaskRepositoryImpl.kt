package com.example.digitalnotebook.db.repositories


import android.util.Log
import com.example.digitalnotebook.R
import com.example.digitalnotebook.db.model.Task
import com.example.digitalnotebook.di.IoDispatcher
import com.example.digitalnotebook.domain.Resource
import com.example.digitalnotebook.domain.await
import com.example.digitalnotebook.presentation.navBar.ROUTE_SIGNUP
import com.example.digitalnotebook.utiles.COLLECTION_PATH_NAME
import com.example.digitalnotebook.utiles.PLEASE_CHECK_INTERNET_CONNECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): TaskRepository {
    override suspend fun addTask(title: String, body: String, ): Resource<Unit> {
        return try {
            withContext(ioDispatcher){
                val tack = hashMapOf(
                    "title" to title,
                    "body" to body,
                    "createdAt" to LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                )
                val addTaskTimeout = withTimeoutOrNull(10000L){
                    firestore.collection(auth.currentUser?.email.toString())
                        .add(tack)
                    Log.d("Email", auth.currentUser?.email.toString())
                }
                if(addTaskTimeout == null){
                    Resource.Failure(IllegalStateException(
                        PLEASE_CHECK_INTERNET_CONNECTION)
                    )
                }
                Resource.Success(Unit)
            }
        }catch (e: Exception){
            Resource.Failure(e)
        }
    }

    override suspend fun getAllTask(): Resource<List<Task>> {
        return try{
            withContext(ioDispatcher){
                val fetchingTasksTimeout = withTimeoutOrNull(10000L){
                    firestore.collection(auth.currentUser?.email.toString())
                        .get()
                        .await()
                        .documents.map { document ->
                            Task(
                                taskId = document.id,
                                title = document.getString("title") ?: "",
                                body = document.getString("body") ?: "",
                                createdAt = document.getString("createdAt")?.let {
                                    LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) // Преобразуем строку в LocalDate
                                } ?: LocalDate.now())
                        }
                }
                if (fetchingTasksTimeout == null){
                    Resource.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }
                Resource.Success(fetchingTasksTimeout?.toList() ?: emptyList())
            }
        }catch (e:Exception){
            Resource.Failure(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Resource<Unit> {
        return try {
            withContext(ioDispatcher){
                val deleteTaskTimeout = withTimeoutOrNull(10000L){
                    firestore.collection(auth.currentUser?.email.toString())
                        .document(taskId)
                        .delete()
                }
                if (deleteTaskTimeout == null){
                    Resource.Failure(IllegalStateException(PLEASE_CHECK_INTERNET_CONNECTION))
                }
                Resource.Success(Unit)
            }
        }catch (e: Exception){
            Resource.Failure(e)
        }
    }

    override suspend fun updateTask(taskId: String, title: String, body: String): Resource<Unit> {
        return try {
            withContext(ioDispatcher){
                val tackUp: Map<String, String> = hashMapOf(
                    "title" to title,
                    "body" to body,
                )
                val updataTaskTimeout = withTimeoutOrNull(10000L){
                    firestore.collection(auth.currentUser?.email.toString())
                        .document()
                        .update(tackUp)
                }
                if(updataTaskTimeout == null){
                    Resource.Failure(IllegalStateException(
                        PLEASE_CHECK_INTERNET_CONNECTION)
                    )
                }
                Resource.Success(Unit)
            }
        }catch (e: Exception){
            Resource.Failure(e)
        }
    }

}