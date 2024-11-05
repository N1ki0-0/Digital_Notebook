package com.example.digitalnotebook.test.MVVM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyViewModel:ViewModel(){
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> get() = _count

    fun increment(){
        _count.value++
    }
}