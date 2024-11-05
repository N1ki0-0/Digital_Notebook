package com.example.digitalnotebook.presentation.state

import com.example.digitalnotebook.db.model.Task
import java.time.LocalDate

data class TasksScreenUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val selectedDateTasks: List<Task> = emptyList(), // Список задач для выбранной даты
    val selectedDate: LocalDate? = null, // Поле для хранения выбранной даты
    val taskDatesWithNotes: Set<LocalDate> = emptySet(), // Даты с заметками
    val errorMessage: String? = null,
    val taskToBeUpdated: Task? = null,
    val isShowAddTaskDialog: Boolean = false,
    val isShowUpdateTaskDialog: Boolean = false,
    val currentTextFieldTitle: String = "",
    val currentTextFieldBody: String = ""
    )
