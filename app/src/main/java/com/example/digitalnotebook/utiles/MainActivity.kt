package com.example.digitalnotebook.utiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.digitalnotebook.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()

        }
    }

}



//Surface(
//modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
//) {
//    val dataStoreContext = LocalContext.current
//    val dataStoreManger = DataStoreManger(dataStoreContext)
//
//    AppContent(
//        this@MainActivity,
//        dataStore,
//        dataStoreManger
//    )