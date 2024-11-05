package com.example.digitalnotebook.presentation.navBar

import com.example.digitalnotebook.R


sealed class NavItem(var label: String, var icon: Int, var route: String)
{
    //    data object Home: NavigationIteam("homeScreen", R.drawable.home, "Home")
    data object Edit: NavItem("Edit", R.drawable.edit, "Edit")
    data object Profile: NavItem("Profile", R.drawable.user,"Profile")
    data object Calendar: NavItem("Calendar", R.drawable.calendar, "Calendar")
}