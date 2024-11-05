package com.example.digitalnotebook.db.model

import java.time.LocalDate

data class Task(
    val taskId: String = "",
    val title: String = "",
    val body: String = "",
    val createdAt: LocalDate
)
