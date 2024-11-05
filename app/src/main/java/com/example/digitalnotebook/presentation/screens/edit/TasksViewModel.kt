package com.example.digitalnotebook.presentation.screens.edit // ktlint-disable package-name

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitalnotebook.db.model.Task
import com.example.digitalnotebook.db.repositories.TaskRepository
import com.example.digitalnotebook.domain.Resource
import com.example.digitalnotebook.presentation.events.TaskScreenSideEffects
import com.example.digitalnotebook.presentation.events.TasksScreenUiEvent
import com.example.digitalnotebook.presentation.state.TasksScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {

    private val _state: MutableStateFlow<TasksScreenUiState> =
        MutableStateFlow(TasksScreenUiState())
    val state: StateFlow<TasksScreenUiState> = _state.asStateFlow()

    private val _effect: Channel<TaskScreenSideEffects> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        sendEvent(TasksScreenUiEvent.GetTasks)
    }

    fun sendEvent(event: TasksScreenUiEvent) {
        reduce(oldState = _state.value, event = event)
    }

    private fun setEffect(builder: () -> TaskScreenSideEffects) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    private fun setState(newState: TasksScreenUiState) {
        _state.value = newState
    }

    private fun reduce(oldState: TasksScreenUiState, event: TasksScreenUiEvent) {
        when (event) {
            is TasksScreenUiEvent.AddTask -> {
                addTask(oldState = oldState, title = event.title, body = event.body)
            }

            is TasksScreenUiEvent.DeleteNote -> {
                deleteNote(oldState = oldState, taskId = event.taskId)
            }

            TasksScreenUiEvent.GetTasks -> {
                getTasks(oldState = oldState)
            }

            is TasksScreenUiEvent.OnChangeAddTsakDialogState -> {
                onChangeAddTaskDialog(oldState = oldState, isShown = event.show)
            }

            is TasksScreenUiEvent.OnChangeUpdateDialogState -> {
                onUpdateAddTaskDialog(oldState = oldState, isShown = event.show)
            }

            is TasksScreenUiEvent.OnChangeTaskBody -> {
                onChangeTaskBody(oldState = oldState, body = event.body)
            }

            is TasksScreenUiEvent.OnChangeTaskTitle -> {
                onChangeTaskTitle(oldState = oldState, title = event.title)
            }

            is TasksScreenUiEvent.SetTaskToBeUpdated -> {
                setTaskToBeUpdated(oldState = oldState, task = event.taskToBeUpdated)
            }

            TasksScreenUiEvent.UpdateNote -> {
                updateNote(oldState = oldState)
            }
        }
    }

    // Метод для получения списка дат, на которые существуют задачи
    fun getTaskDatesWithNotes(): Set<LocalDate> {
        return _state.value.tasks.map { it.createdAt }.toSet() // Возвращаем уникальные даты с задачами
    }

    // Функция для фильтрации задач по выбранной дате
    fun selectDate(date: LocalDate) {
        _state.update { oldState ->
            val filteredTasks = oldState.tasks.filter { task ->
                task.createdAt == date
            }
            oldState.copy(
                selectedDate = date,
                selectedDateTasks = filteredTasks
            )
        }
    }

    private fun addTask(title: String, body: String, oldState: TasksScreenUiState) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            when (val result = taskRepository.addTask(title = title, body = body)) {
                is Resource.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when adding task"
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Resource.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currentTextFieldTitle = "",
                            currentTextFieldBody = "",
                        ),
                    )

                    sendEvent(TasksScreenUiEvent.OnChangeAddTsakDialogState(show = false))

                    sendEvent(TasksScreenUiEvent.GetTasks)

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Task added successfully") }
                }

                Resource.Loading -> TODO()
            }
        }
    }

    private fun getTasks(oldState: TasksScreenUiState) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            when (val result = taskRepository.getAllTask()) {
                is Resource.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when getting your task"
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Resource.Success -> {
                    val tasks = result.result
                    setState(
                        oldState.copy(
                            isLoading = false,
                            tasks = tasks,
                        )
                    )
                }

                Resource.Loading -> TODO()
            }
        }
    }

    private fun deleteNote(oldState: TasksScreenUiState, taskId: String) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            when (val result = taskRepository.deleteTask(taskId = taskId)) {
                is Resource.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when deleting task"
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Resource.Success -> {
                    setState(oldState.copy(isLoading = false))

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Task deleted successfully") }

                    sendEvent(TasksScreenUiEvent.GetTasks)
                }

                Resource.Loading -> TODO()
            }
        }
    }

    private fun updateNote(oldState: TasksScreenUiState) {
        viewModelScope.launch {
            setState(oldState.copy(isLoading = true))

            val title = oldState.currentTextFieldTitle
            val body = oldState.currentTextFieldBody
            val taskToBeUpdated = oldState.taskToBeUpdated

            when (
                val result = taskRepository.updateTask(
                    title = title,
                    body = body,
                    taskId = taskToBeUpdated?.taskId ?: "",
                )
            ) {
                is Resource.Failure -> {
                    setState(oldState.copy(isLoading = false))

                    val errorMessage =
                        result.exception.message ?: "An error occurred when updating task"
                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = errorMessage) }
                }

                is Resource.Success -> {
                    setState(
                        oldState.copy(
                            isLoading = false,
                            currentTextFieldTitle = "",
                            currentTextFieldBody = "",
                        ),
                    )

                    sendEvent(TasksScreenUiEvent.OnChangeUpdateDialogState(show = false))

                    setEffect { TaskScreenSideEffects.ShowSnackBarMessage(message = "Task updated successfully") }

                    sendEvent(TasksScreenUiEvent.GetTasks)
                }

                Resource.Loading -> TODO()
            }
        }
    }

    private fun onChangeAddTaskDialog(oldState: TasksScreenUiState, isShown: Boolean) {
        setState(oldState.copy(isShowAddTaskDialog = isShown))
    }

    private fun onUpdateAddTaskDialog(oldState: TasksScreenUiState, isShown: Boolean) {
        setState(oldState.copy(isShowUpdateTaskDialog = isShown))
    }

    private fun onChangeTaskBody(oldState: TasksScreenUiState, body: String) {
        setState(oldState.copy(currentTextFieldBody = body))
    }

    private fun onChangeTaskTitle(oldState: TasksScreenUiState, title: String) {
        setState(oldState.copy(currentTextFieldTitle = title))
    }

    private fun setTaskToBeUpdated(oldState: TasksScreenUiState, task: Task) {
        setState(oldState.copy(taskToBeUpdated = task))
    }
}
