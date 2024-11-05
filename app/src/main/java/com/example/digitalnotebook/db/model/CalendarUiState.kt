package com.example.digitalnotebook.db.model

import java.time.LocalDate
import java.time.YearMonth


// Состояние пользовательского интерфейса календаря
data class CalendarUiState(
    val yearMonth: YearMonth, // Текущий месяц и год
    val dates: List<Date>, // Список дат для отображения
    val selectedDate: LocalDate? = null, //Поле для хранения выбранной даты
    val taskDatesWithNotes: Set<LocalDate> = emptySet() // Новый параметр для хранения дат с заметками

) {
    companion object {
        // Начальное состояние: текущий месяц и пустой список дат
        val Init = CalendarUiState(
            yearMonth = YearMonth.now(),
            dates = emptyList(),
            selectedDate = LocalDate.now()
        )
    }

    // Вложенный класс для представления даты в календаре
    data class Date(
        val dayOfMonth: String, // День месяца в виде строки
        val isSelected: Boolean, // Является ли дата выбранной (например, текущий день)
        val hasTask: Boolean
    ) {
        companion object {
            // Пустой объект Date для инициализации
            val Empty = Date("", false, false)
        }
    }
}



