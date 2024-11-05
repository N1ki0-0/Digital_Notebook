package com.example.digitalnotebook.test.MVVM

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Count(viewModel: MyViewModel = viewModel()){
    val count by viewModel.count.collectAsState()
    Column (modifier = Modifier.fillMaxSize()){
        Text(text = "$count")
        Button(onClick = { viewModel.increment() }) {
            Text(text = "Увеличить")
        }
    }
}