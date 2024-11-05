package com.example.digitalnotebook

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import com.example.digitalnotebook.presentation.navBar.NavHostContainer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.digitalnotebook.presentation.navBar.NavItem


@Composable
fun MainScreen() {

    val navItemList = listOf(
        NavItem.Edit,
        NavItem.Calendar,
        NavItem.Profile,
    )

    val navController = rememberNavController()
    val isShowBottomBar = remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = {
            if(isShowBottomBar.value) {
                BottomNavigation(
                    backgroundColor = colorResource(id = R.color.black),
                    contentColor = Color.White
                )
                {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    navItemList.forEach{items ->
                        BottomNavigationItem(
                            icon = {
                                androidx.compose.material.Icon(
                                    painterResource(id = items.icon),
                                    contentDescription = items.label
                                )
                            },
                            selectedContentColor = Color.White,
                            unselectedContentColor = Color.White.copy(0.4f),
                            alwaysShowLabel = true,
                            selected = currentRoute == items.route,
                            onClick = { navController.navigate(items.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true }
                            }
                        )
                    }
                }
            }
        }, content = { padding ->
            NavHostContainer(
                navController = navController,
                isShowBottomBar = isShowBottomBar,
                context = LocalContext.current,
                padding = padding
            )
        },
       // backgroundColor = colorResource(R.color.white)
    )
}
