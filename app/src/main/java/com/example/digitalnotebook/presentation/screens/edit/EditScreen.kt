package com.example.digitalnotebook.presentation.screens.edit

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.digitalnotebook.presentation.events.TaskScreenSideEffects
import com.example.digitalnotebook.presentation.events.TasksScreenUiEvent
import com.example.digitalnotebook.ui.theme.Xoli
import com.example.digitalnotebook.utiles.SIDE_EFFECTS_KEY
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun Edit(
         tasksViewModel: TasksViewModel = hiltViewModel()
){

    val uiState = tasksViewModel.state.collectAsState().value
    val effectFlow = tasksViewModel.effect

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = SIDE_EFFECTS_KEY) {
        effectFlow.onEach { effect ->
            when (effect) {
                is TaskScreenSideEffects.ShowSnackBarMessage -> {
                    snackBarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short,
                        actionLabel = "DISMISS",
                    )
                }
            }
        }.launchIn(this) // Запускаем поток в текущем корутинном контексте
    }

    Scaffold(
//        modifier = Modifier.background(Xoli),
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        floatingActionButton = {
            Column {
                ExtendedFloatingActionButton(
                    icon = {
                        Icon(
                            Icons.Rounded.AddCircle,
                            contentDescription = "Add Task",
                            tint = Color.White,
                        )
                    },
                    text = {
                        Text(
                            text = "Add Task",
                            color = Color.White,
                        )
                    },
                    onClick = {
                        tasksViewModel.sendEvent(
                            event = TasksScreenUiEvent.OnChangeAddTsakDialogState(show = true),
                        )
                    },
                    modifier = Modifier.padding(horizontal = 12.dp),
                    containerColor = Color.Black,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                )
            }
        },
        containerColor = Xoli,
    ) {
        Box(modifier = Modifier.padding(it)) {
            when {
                uiState.isLoading -> {
                    LoadingComponent()
                }

                !uiState.isLoading && uiState.tasks.isNotEmpty() -> {
                    LazyColumn(contentPadding = PaddingValues(14.dp)) {
                        item {
                            WelcomeMessageComponent()

                            androidx.compose.foundation.layout.Spacer(
                                modifier = Modifier.height(
                                    30.dp,
                                ),
                            )
                        }

                        items(uiState.tasks) { task ->
                            TaskCardComponent(task = task, deleteTask = { taskId ->
                                    Log.d("TASK_ID: ", taskId)
                                    tasksViewModel.sendEvent(
                                        event = TasksScreenUiEvent.DeleteNote(taskId = taskId),
                                    )
                                }, updateTask = { taskToBeUpdated ->
                                    tasksViewModel.sendEvent(
                                        TasksScreenUiEvent.OnChangeUpdateDialogState(
                                            show = true,
                                        ),
                                    )
                                    tasksViewModel.sendEvent(
                                        event = TasksScreenUiEvent.SetTaskToBeUpdated(
                                            taskToBeUpdated = taskToBeUpdated,
                                        ),
                                    )
                                },)
                        }
                    }
                }

                !uiState.isLoading && uiState.tasks.isEmpty() -> {
                    EmptyComponent()
                }
            }
        }
        // Код
        if (uiState.isShowAddTaskDialog) {
            AddTaskDialogComponent(
                uiState = uiState,
                setTaskTitle = { title ->
                    tasksViewModel.sendEvent(
                        event = TasksScreenUiEvent.OnChangeTaskTitle(title = title),
                    )
                },
                setTaskBody = { body ->
                    tasksViewModel.sendEvent(
                        event = TasksScreenUiEvent.OnChangeTaskBody(body = body),
                    )
                },
                saveTask = {
                    tasksViewModel.sendEvent(
                        event = TasksScreenUiEvent.AddTask(
                            title = uiState.currentTextFieldTitle,
                            body = uiState.currentTextFieldBody,
                        ),
                    )
                },
                closeDialog = {
                    tasksViewModel.sendEvent(
                        event = TasksScreenUiEvent.OnChangeAddTsakDialogState(show = false),
                    )
                },
            )
        }

        if (uiState.isShowUpdateTaskDialog) {
            UpdateTaskDialogComponent(
                uiState = uiState,
                setTaskTitle = { title ->
                    tasksViewModel.sendEvent(
                        event = TasksScreenUiEvent.OnChangeTaskTitle(title = title),
                    )
                },
                setTaskBody = { body ->
                    tasksViewModel.sendEvent(
                        event = TasksScreenUiEvent.OnChangeTaskBody(body = body),
                    )
                },
                saveTask = {
                    tasksViewModel.sendEvent(event = TasksScreenUiEvent.UpdateNote)
                },
                closeDialog = {
                    tasksViewModel.sendEvent(
                        event = TasksScreenUiEvent.OnChangeUpdateDialogState(show = false),
                    )
                },
                task = uiState.taskToBeUpdated,
            )
        }
    }
}