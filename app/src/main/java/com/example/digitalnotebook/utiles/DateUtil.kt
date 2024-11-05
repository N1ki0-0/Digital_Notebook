package com.example.digitalnotebook.utiles

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

// Объект для работы с датами, предоставляющий локализованные названия дней недели
object DateUtil {
    // Ленивая инициализация массива с именами дней недели
    val daysOfWeek: Array<String>
        get() {
            val daysOfWeek = Array(7) { "" } // Массив для хранения названий дней недели
            // Перебираем все дни недели (понедельник, вторник и т.д.)
            for (dayOfWeek in DayOfWeek.entries) {
                // Получаем локализованное краткое имя дня (например, "Пн" для понедельника)
                val localizedDayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                daysOfWeek[dayOfWeek.value - 1] = localizedDayName // Сохраняем в массив по индексу
            }
            return daysOfWeek // Возвращаем заполненный массив
        }
}

// Расширение для класса YearMonth, возвращающее список всех дней месяца, начиная с понедельника
fun YearMonth.getDayOfMonthStartingFromMonday(): List<LocalDate> {
    val firstDayOfMonth = LocalDate.of(year, month, 1) // Первый день текущего месяца
    val firstMondayOfMonth = firstDayOfMonth.with(DayOfWeek.MONDAY) // Первый понедельник месяца
    val firstDayOfNextMonth = firstDayOfMonth.plusMonths(1) // Первый день следующего месяца
    return generateSequence(firstMondayOfMonth) { it.plusDays(1) } // Генерируем последовательность дней
        .takeWhile { it.isBefore(firstDayOfNextMonth) } // Прерываем, когда доходим до следующего месяца
        .toList() // Преобразуем в список
}

// Расширение для YearMonth, возвращающее название месяца и год в формате строки
fun YearMonth.getDisplayName(): String {
    // Возвращаем название месяца (локализованное) и год (например, "Октябрь 2024")
    return "${month.getDisplayName(TextStyle.FULL, Locale.getDefault())} $year"
}