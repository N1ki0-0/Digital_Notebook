package com.example.digitalnotebook.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
):ViewModel() {

    //хранение состояния аунтификации пользователя при входе
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    //хранение состояния аунтификации пользователя при регистрации
    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    //Получение текущих данных пользователя из репозитория
    val currentUser: FirebaseUser?
        get() = repository.currentUser

    //Инициализация ViewModel: есть ли пользователь который уже прошел аунитфикацию
    init{
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    // Функция для аутентификации пользователя с использованием электронной почты и пароля
    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value =
            Resource.Loading // Установка состояния загрузки перед выполнением аутентификации
        val result = repository.login(email, password) // Вызов репозитория для выполнения аутентификации
        _loginFlow.value = result // Обновление состояния входа с результатом
    }

    // Функция для регистрации нового пользователя с использованием электронной почты и пароля
    fun signup(name: String, email: String, password: String) = viewModelScope.launch {
        _signupFlow.value =
            Resource.Loading // Установка состояния загрузки перед выполнением регистрации
        val result = repository.signup(name, email, password) // Вызов репозитория для выполнения регистрации
        _signupFlow.value = result // Обновление состояния регистрации с результатом
    }

    fun logout(){
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }

}