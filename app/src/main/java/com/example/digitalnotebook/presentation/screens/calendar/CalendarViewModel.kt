package com.example.digitalnotebook.presentation.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitalnotebook.db.model.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

// ViewModel для календаря, управляющая состоянием интерфейса и логикой смены месяцев
class CalendarViewModel : ViewModel() {
    private val dataSource by lazy { CalendarDataSource() } // Инициализация источника данных

    // Поток для хранения состояния пользовательского интерфейса
    private val _uiState = MutableStateFlow(CalendarUiState.Init)
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        // Инициализация состояния при создании ViewModel
        updateDates()

    }

    // Функция для получения дат с задачами и обновления календаря
    fun updateTaskDates(taskDatesWithNotes: Set<LocalDate>) {
        _uiState.update { currentState ->
            currentState.copy(
                dates = dataSource.getDates(currentState.yearMonth, currentState.selectedDate, taskDatesWithNotes)
            )
        }
    }

    private fun updateDates(){
        _uiState.update { currentState->
            currentState.copy(
                // Обновляем текущий месяц списком дат для отображения
                dates = dataSource.getDates(currentState.yearMonth,
                    currentState.selectedDate,
                    taskDatesWithNotes = currentState.taskDatesWithNotes) // Обновляем даты с заметками
            )
        }
    }

    // Функция для изменения выбранной даты
    fun onDateSelected(date: LocalDate){
        _uiState.update { currentState ->
            currentState.copy(selectedDate = date)
        }
        updateDates()
    }

    // Метод для перехода на следующий месяц
    fun toNextMonth(nextMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = nextMonth, // Обновляем месяц

                )
            }
        }
        updateDates()
    }

    // Метод для перехода на предыдущий месяц
    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = prevMonth, // Обновляем месяц

                )
            }
        }
        updateDates()
    }
}