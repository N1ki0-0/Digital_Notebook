package com.example.digitalnotebook.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.digitalnotebook.R
import com.example.digitalnotebook.domain.AuthViewModel
import com.example.digitalnotebook.presentation.navBar.NavItem
import com.example.digitalnotebook.presentation.navBar.ROUTE_LOGIN
import com.example.digitalnotebook.ui.theme.Xoli
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Profile(navControler: NavHostController, authViewModel: AuthViewModel = hiltViewModel()){


    Column (modifier = Modifier
        .fillMaxSize()
        .background(Xoli),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 10.dp)
                .height(55.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
//                if (userData?.profilePictureUrl != null) {
//                    AsyncImage(
//                        model = userData.profilePictureUrl, null, modifier = Modifier
//                            .padding(end = 5.dp)
//                            .size(60.dp)
//                            .clip(CircleShape)
//                            .clickable {},
//                        contentScale = ContentScale.Crop
//                    )
//                } else {
                    Image(
                        painter = painterResource(id = R.drawable.cat_), null, modifier = Modifier
                            .padding(end = 5.dp)
                            .size(60.dp)
                            .clip(CircleShape)
                            .clickable {},
                        contentScale = ContentScale.Crop
                    )
//                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(1f)
            ) {
                if (authViewModel.currentUser?.displayName != null) {
                    Text(
                        text = "Name ${authViewModel?.currentUser?.displayName}",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = "Name",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            IconButton(onClick = { authViewModel.logout()
                navControler.navigate(ROUTE_LOGIN) {
                    popUpTo(NavItem.Profile.route) { inclusive = true }
                }}) {
                Image(
                    painter = painterResource(id = R.drawable.exit),
                    null,
                    modifier = Modifier
                        .padding(end = 5.dp)
                )
            }

        }

    }
}

class MyViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> get() = _count

    fun increment() {
        _count.value++
    }
}