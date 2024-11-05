package com.example.digitalnotebook.presentation.screens.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.digitalnotebook.R
import com.example.digitalnotebook.db.model.CalendarUiState
import com.example.digitalnotebook.presentation.events.TaskScreenSideEffects
import com.example.digitalnotebook.presentation.events.TasksScreenUiEvent
import com.example.digitalnotebook.presentation.screens.edit.EmptyComponent
import com.example.digitalnotebook.presentation.screens.edit.LoadingComponent
import com.example.digitalnotebook.presentation.screens.edit.TaskCardComponent
import com.example.digitalnotebook.presentation.screens.edit.TasksViewModel
import com.example.digitalnotebook.presentation.screens.edit.WelcomeMessageComponent
import com.example.digitalnotebook.ui.theme.Xoli
import com.example.digitalnotebook.utiles.DateUtil
import com.example.digitalnotebook.utiles.SIDE_EFFECTS_KEY
import com.example.digitalnotebook.utiles.getDisplayName
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavHostController, // Навигация для возможного перехода к другим экранам
    viewModel: CalendarViewModel = hiltViewModel(), // Инициализация ViewModel через Hilt
    tasksViewModel: TasksViewModel = hiltViewModel()
) {
    // Получаем состояние UI из ViewModel в виде State
    val uiState by viewModel.uiState.collectAsState()
    val tasksUiState by tasksViewModel.state.collectAsState()

    // Получаем даты с заметками из TasksViewModel
    LaunchedEffect(tasksUiState.tasks) {
        val taskDatesWithNotes = tasksViewModel.getTaskDatesWithNotes()
        viewModel.updateTaskDates(taskDatesWithNotes) // Передаем даты в CalendarViewModel
    }

    Surface(
        modifier = Modifier
            .fillMaxSize() // Заполнение всей доступной области
            .verticalScroll(rememberScrollState()) // Вертикальная прокрутка

    ) {
        // Виджет календаря с передачей текущих данных состояния UI
        CalendarWidget(
            days = DateUtil.daysOfWeek, // Названия дней недели
            yearMonth = uiState.yearMonth, // Текущий месяц и год
            dates = uiState.dates, // Список дат для отображения
            selectedDate = uiState.selectedDate, // Передаем выбранную дату
            onPreviousMonthButtonClicked = { prevMonth ->
                viewModel.toPreviousMonth(prevMonth) // Переход к предыдущему месяцу
            },
            onNextMonthButtonClicked = { nextMonth ->
                viewModel.toNextMonth(nextMonth) // Переход к следующему месяцу
            },
            onDateClickListener = { date->
                if (date.dayOfMonth.isNotEmpty()) {
                    val selectedDate = uiState.yearMonth.atDay(date.dayOfMonth.toInt())
                    viewModel.onDateSelected(selectedDate)
                    tasksViewModel.selectDate(selectedDate) // Обновляем задачи для выбранной даты
                }

                // TODO: Добавить логику при клике на дату
            }
        )
        // Список задач для выбранной даты
        if (tasksUiState.selectedDateTasks.isNotEmpty()) {
            LazyColumn {
                items(tasksUiState.selectedDateTasks) { task ->
                    TaskCardComponent(
                        deleteTask = { taskId -> tasksViewModel.sendEvent(TasksScreenUiEvent.DeleteNote(taskId)) },
                        updateTask = { updatedTask -> tasksViewModel.sendEvent(TasksScreenUiEvent.SetTaskToBeUpdated(updatedTask)) },
                        task = task
                    )
                }
            }
        } else {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


// Виджет календаря, отображающий дни недели, заголовок и содержимое
@Composable
fun CalendarWidget(
    days: Array<String>, // Названия дней недели
    yearMonth: YearMonth, // Текущий месяц и год
    dates: List<CalendarUiState.Date>, // Список дат
    selectedDate: LocalDate?, // Выбранная дата (новый параметр)
    onPreviousMonthButtonClicked: (YearMonth) -> Unit, // Обработчик клика на предыдущий месяц
    onNextMonthButtonClicked: (YearMonth) -> Unit, // Обработчик клика на следующий месяц
    onDateClickListener: (CalendarUiState.Date) -> Unit, // Обработчик клика на дату
) {
    Column(
        modifier = Modifier
            .background(Xoli)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Отображение дней недели
        Row {
            repeat(days.size) {
                val item = days[it]
                DayItem(item, modifier = Modifier.weight(1f)) // День недели в ячейке
            }
        }
        // Заголовок для отображения месяца и навигации по месяцам
        Header(
            yearMonth = yearMonth,
            onPreviousMonthButtonClicked = onPreviousMonthButtonClicked,
            onNextMonthButtonClicked = onNextMonthButtonClicked
        )
        // Содержимое календаря (даты месяца)
        Content(
            dates = dates,
            onDateClickListener = onDateClickListener,
            selectedDate = selectedDate
        )


    }
}



// Заголовок календаря с кнопками для навигации между месяцами
@Composable
fun Header(
    yearMonth: YearMonth, // Текущий месяц и год
    onPreviousMonthButtonClicked: (YearMonth) -> Unit, // Обработчик для перехода на предыдущий месяц
    onNextMonthButtonClicked: (YearMonth) -> Unit // Обработчик для перехода на следующий месяц
) {
    Row {
        // Кнопка для перехода на предыдущий месяц
        IconButton(onClick = {
            onPreviousMonthButtonClicked.invoke(yearMonth.minusMonths(1))
        }) {
            Icon(
                painter = painterResource(id = R.drawable.angel1),
                contentDescription = stringResource(id = R.string.back)
            )
        }
        // Название месяца и года по центру
        Text(
            text = yearMonth.getDisplayName(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        // Кнопка для перехода на следующий месяц
        IconButton(onClick = {
            onNextMonthButtonClicked.invoke(yearMonth.plusMonths(1))
        }) {
            Icon(
                painter = painterResource(id = R.drawable.angle2),
                contentDescription = stringResource(id = R.string.next)
            )
        }
    }
}

// Компонент для отображения одного дня недели (например, "Пн", "Вт")
@Composable
fun DayItem(day: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        )
    }
}

// Основное содержимое календаря, отображающее даты месяца
@Composable
fun Content(
    dates: List<CalendarUiState.Date>, // Список объектов Date с данными для отображения
    selectedDate: LocalDate?,
    onDateClickListener: (CalendarUiState.Date) -> Unit // Обработчик клика по дате
) {
    Column {
        var index = 0
        repeat(6) { // Максимум 6 строк, чтобы покрыть все дни месяца
            if (index >= dates.size) return@repeat
            Row {
                repeat(7) { // 7 столбцов (дни недели)
                    val item = if (index < dates.size) dates[index] else CalendarUiState.Date.Empty

                    // Отображаем одну ячейку календаря с датой
                    ContentItem(
                        date = item,
                        onClickListener = onDateClickListener,
                        modifier = Modifier.weight(1f),
                        isSelected = item.dayOfMonth == selectedDate?.dayOfMonth.toString()// Проверяем, выбрана ли дата
                    )
                    index++
                }
            }
        }
    }
}

// Отображение одной ячейки календаря с датой
@Composable
fun ContentItem(
    date: CalendarUiState.Date, // Объект Date для отображения в ячейке
    onClickListener: (CalendarUiState.Date) -> Unit, // Обработчик клика на дату
    modifier: Modifier = Modifier,
    isSelected: Boolean // Флаг указывающий выбрана ли текущая дата
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        date.hasTask -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f) // Цвет для даты с задачей
        else -> Color.Transparent
    }
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable {
                onClickListener(date) // Вызов обработчика при клике
            }
    ) {
        Text(
            text = date.dayOfMonth, // Текст с днем месяца
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun Col(){
    val navControler = rememberNavController()
    CalendarScreen(navControler)
}