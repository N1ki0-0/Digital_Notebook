package com.example.digitalnotebook.presentation.navBar

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.digitalnotebook.presentation.screens.profile.LoginScreen
import com.example.digitalnotebook.presentation.screens.edit.Edit
import com.example.digitalnotebook.presentation.screens.profile.Profile
import com.example.digitalnotebook.presentation.screens.profile.SignupScreen
import com.example.digitalnotebook.presentation.screens.calendar.CalendarScreen

@Composable
fun NavHostContainer(navController: NavHostController,
                     isShowBottomBar: MutableState<Boolean>,
                     context: Context,
                     padding: PaddingValues){


    NavHost(
        navController = navController,
        modifier = Modifier.padding(paddingValues = padding),
        startDestination = ROUTE_LOGIN,
        builder = {
            composable(NavItem.Edit.route){
                Edit()
                isShowBottomBar.value = true
            }
            composable(NavItem.Profile.route){
                Profile(navController)
                isShowBottomBar.value = true
            }
            composable(NavItem.Calendar.route){
                CalendarScreen(navController)
            }
            composable(ROUTE_LOGIN) {
                LoginScreen(navController)
                isShowBottomBar.value = false
            }
            composable(ROUTE_SIGNUP) {
                SignupScreen(navController)
                isShowBottomBar.value = false
            }
        }
    )
}