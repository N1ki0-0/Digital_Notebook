package com.example.digitalnotebook.presentation.screens.calendar

import com.example.digitalnotebook.db.model.CalendarUiState
import com.example.digitalnotebook.utiles.getDayOfMonthStartingFromMonday
import java.time.LocalDate
import java.time.YearMonth

// Источник данных для календаря
class CalendarDataSource {

    // Метод получения списка дат для заданного месяца и года
    fun getDates(yearMonth: YearMonth,
                 selectedDate: LocalDate?,
                 taskDatesWithNotes: Set<LocalDate> // Новый параметр для выделения дат с заметками
    ): List<CalendarUiState.Date> {
        return yearMonth.getDayOfMonthStartingFromMonday() // Получаем все дни начиная с понедельника
            .map { date ->
                // Создаем объект даты для интерфейса
                CalendarUiState.Date(
                    dayOfMonth = if (date.monthValue == yearMonth.monthValue) {
                        "${date.dayOfMonth}" // Дата текущего месяца
                    } else {
                        "" // Пустая строка для дат вне текущего месяца
                    },
                    isSelected = date == selectedDate, // Выделяем выбранную дат
                    hasTask = taskDatesWithNotes.contains(date) // Проверяем, есть ли задачи на дату
                )
            }
    }
}